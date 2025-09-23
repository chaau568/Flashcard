import { useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import type { TypeCard } from "../type/TypeCard";
import style from "./DeckPublic.module.css";

interface ApiResponseWithData<T> {
  message: string;
  status: number;
  data: T;
}

const DeckPublic = () => {
  const [cards, setCards] = useState<TypeCard[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const { deckId } = useParams<string>();
  const location = useLocation();
  const { deckName } = location.state || {};

  const [currentIndex, setCurrentIndex] = useState<number>(0);
  const [no, setNo] = useState<number>(1);
  const [showAnswer, setShowAnswer] = useState<boolean>(false);
  const [finished, setFinished] = useState(false);

  const currentCard = cards[currentIndex];

  useEffect(() => {
    document.body.classList.add(style.deck_page);

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

    return () => {
      document.body.classList.remove(style.deck_page);
    };
  }, [deckId]);

  if (loading) return <p>Loading...</p>;
  if (cards.length === 0) return <p>No cards in this deck.</p>;

  if (finished) {
    return (
      <div className={style.deck_container}>
        <h2>Deck Finished!</h2>
        <button onClick={() => window.history.back()}>Exit</button>
      </div>
    );
  }

  const handleNext = () => {
    if (currentIndex + 1 >= cards.length) {
      setFinished(true);
    } else {
      setCurrentIndex(currentIndex + 1);
      setNo(no + 1);
      setShowAnswer(false);
    }
  };

  return (
    <div className={style.deck_container}>
      <h2>Deck Name: {deckName}</h2>

      <div className={style.exit}>
        <button onClick={() => window.history.back()}>Exit</button>
      </div>

      <div className={style.card}>
        <div className={style.no}>
          <h4>
            {no}/{cards.length}
          </h4>
        </div>
        {!showAnswer ? (
          <p>Q: {currentCard.frontCard}</p>
        ) : (
          <p>A: {currentCard.backCard}</p>
        )}
      </div>

      <div className={style.buttons}>
        {!showAnswer ? (
          <button onClick={() => setShowAnswer(true)}>Show Answer</button>
        ) : (
          <button onClick={handleNext}>Next</button>
        )}
      </div>
    </div>
  );
};

export default DeckPublic;
