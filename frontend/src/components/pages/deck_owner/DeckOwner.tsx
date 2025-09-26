import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import type { TypeCard } from "../type/TypeCard";
import type { TypeCardResult } from "../type/TypeCardResult";
import type { TypeDeck } from "../type/TypeDeck";
import { format } from "date-fns";
import style from "./DeckOwner.module.css";

interface ApiResponseWithData<T> {
  message: string;
  status: number;
  data: T;
}

const DeckOwner = () => {
  const [step, setStep] = useState<1 | 2 | 3>(1);
  const [ownerUsername, setOwnerUsername] = useState<string>("");
  const [tagList, setTagList] = useState<string[]>([]);
  const [createAt, setCreateAt] = useState<string>("");
  const [updateAt, setUpdateAt] = useState<string>("");
  const [cards, setCards] = useState<TypeCard[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const { deckId } = useParams<string>();
  const location = useLocation();
  const { deckName } = location.state || {};

  const [currentIndex, setCurrentIndex] = useState<number>(0);
  const [no, setNo] = useState<number>(1);
  const [showAnswer, setShowAnswer] = useState<boolean>(false);
  const [finished, setFinished] = useState(false);

  const [results, setResults] = useState<TypeCardResult[]>([]);
  const naviaget = useNavigate();

  const currentCard = cards[currentIndex];
  const [isEdit, setIsEdit] = useState<boolean>(false);

  const handleProgress = (progress: "easy" | "normal" | "hard" | "again") => {
    if (currentCard) {
      const newResult: TypeCardResult = {
        cardId: currentCard.id,
        progress,
      };
      setResults((prev) => [...prev, newResult]);
    }
    setShowAnswer(true);
  };

  useEffect(() => {
    document.body.classList.add(style.deck_page);

    const fetchDeck = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/flashcard/deck/get_info/${deckId}`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
          }
        );

        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

        const json: ApiResponseWithData<TypeDeck> = await res.json();
        setOwnerUsername(json.data.ownerUsername);
        setTagList(json.data.tagList);

        const formatDate1 = new Date(json.data.createAt)
        const formatDate2 = new Date(json.data.updateAt)

        const readableCreated  = format(formatDate1, "dd/MM/yyyy HH:mm:ss");
        const readableUpdated  = format(formatDate2, "dd/MM/yyyy HH:mm:ss");

        setCreateAt(readableCreated);
        setUpdateAt(readableUpdated);
      } catch (error) {
        alert("Failed to fetch cards: " + error);
      } finally {
        setLoading(false);
      }
    };

    if (deckId) fetchDeck();

    const fetchCards = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/flashcard/card/get_by_deck_id/${deckId}`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
          }
        );

        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

        const json: ApiResponseWithData<TypeCard[]> = await res.json();
        setCards(json.data);
      } catch (error) {
        alert("Failed to fetch cards: " + error);
      } finally {
        setLoading(false);
      }
    };

    if (deckId) fetchCards();

    if (finished) {
      naviaget("/deck_finish", { state: { deckId, deckName, results } });
    }

    return () => {
      document.body.classList.remove(style.deck_page);
    };
  }, [finished, deckId]);

  useEffect(() => {
    if (isEdit) {
      naviaget(`/deck_update/${deckId}`);
    }
  }, [isEdit, deckId]);

  if (loading) return <p>Loading...</p>;

  const handleRemoveDeck = async () => {
    if (!deckId) return;
    try {
      const res = await fetch(
        `http://localhost:8080/flashcard/deck/delete/${deckId}`,
        {
          method: "DELETE",
          credentials: "include",
        }
      );

      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
      alert("Deck deleted successfully!");
      naviaget("/inventory");
    } catch (error) {
      alert("Failed to delete deck: " + error);
    }
  };

  const checkCanPlay = () => {
    if (cards.length === 0) {
      setStep(3);
    } else {
      setStep(2);
    }
  }

  return (
    <div className={style.deck_container}>
      <div className={style.exit}>
        <button onClick={() => window.history.back()}>Exit</button>
      </div>
      {step === 1 && (
        <div className={style.deck_step1}>
          <div className={style.deck_step1_content}>
            <div>
              <h2>{deckName}</h2>
            </div>
            <div className={style.tags}>
              <h3>Tags: </h3>
              {tagList.map((tag, index) => (
                <span key={index} className={style.tag}>
                  {tag}
                </span>
              ))}
            </div>
            <h3>Owner User: {ownerUsername}</h3>
            <p>Cards Number: {cards.length}</p>
            <p>Create At: {createAt}</p>
            <p>Update At: {updateAt}</p>
          </div>
          <div className={style.deck_step1_btn}>
            <button onClick={() => setIsEdit(true)}>Edit</button>
            <button onClick={checkCanPlay}>Play</button>
            <button
              onClick={() => {
                const confirmed = window.confirm(
                  "Are you sure you want to delete this deck?"
                );
                if (confirmed) {
                  handleRemoveDeck();
                }
              }}
            >
              Remove
            </button>
          </div>
        </div>
      )}

      {step === 2 && (
        <div className={style.deck_step2}>
          <h2>Deck Name: {deckName}</h2>
          <div className={style.card}>
            <div className={style.no}>
              <h4>
                {no}/{cards.length}
              </h4>
            </div>
            {!showAnswer ? (
              <>
                <p>Q: {currentCard.frontCard}</p>
              </>
            ) : (
              <>
                <p>A: {currentCard.backCard}</p>
              </>
            )}
          </div>

          <div className={style.buttons}>
            {!showAnswer ? (
              <button onClick={() => setShowAnswer(true)}>Show Answer</button>
            ) : (
              ["easy", "normal", "hard", "again"].map((progress) => (
                <button
                  key={progress}
                  onClick={() => {
                    handleProgress(
                      progress as "easy" | "normal" | "hard" | "again"
                    );
                    // ไป card ถัดไปทันที
                    if (currentIndex + 1 >= cards.length) {
                      setFinished(true); // หมด deck
                    } else {
                      setCurrentIndex(currentIndex + 1);
                      setNo(no + 1);
                      setShowAnswer(false); // reset สำหรับ card ถัดไป
                    }
                  }}
                >
                  {progress.toUpperCase()}
                </button>
              ))
            )}
          </div>
        </div>
      )}

      {step === 3 && (
        <div>
          <h3>There are no cards to play yet.</h3>
        </div>
      )}
    </div>
  );
};

export default DeckOwner;
