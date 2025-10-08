import { useEffect, useState } from "react";
import style from "./DeckCreate.module.css";
import type { TypeCardInput } from "../type/TypeCardInput";
import type { TypeDeckCreate } from "../type/TypeDeckCreate";
import { useNavigate } from "react-router-dom";

interface ApiResponse {
  message: string;
  status: number;
}

interface ApiResponseWithData<T> {
  message: string;
  status: number;
  data: T;
}

const DeckCreate = () => {
  const [step, setStep] = useState<1 | 2 | 3>(1);

  const [loadding, setLoadding] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const [isPublic, setIsPublic] = useState<boolean>(false);
  const [deckName, setDeckName] = useState("");
  const [nameTags, setNameTags] = useState<string[]>([]);
  const [tagInput, setTagInput] = useState("");
  const [deckId, setDeckId] = useState<string>("");

  const [cards, setCards] = useState<TypeCardInput[]>([]);
  const [currentCard, setCurrentCard] = useState<TypeCardInput>({
    frontCard: "",
    backCard: "",
  });

  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add(style.deck_create_page);

    return () => {
      document.body.classList.remove(style.deck_create_page);
    };
  }, []);

  useEffect(() => {
    console.log("deckId: " + deckId);
  }, [deckId]);

  // ✅ เพิ่ม tag
  const addTag = () => {
    if (tagInput.trim() !== "") {
      setNameTags([...nameTags, tagInput.trim()]);
      setTagInput("");
    }
  };

  // ✅ ลบ tag
  const removeTag = (index: number) => {
    setNameTags(nameTags.filter((_, i) => i !== index));
  };

  // ✅ เมื่อกด Create Deck → ไป step 2
  const handleCreateDeck = async () => {
    if (!deckName.trim()) {
      setErrorMessage("Please enter deck name");
      return;
    }

    setLoadding(true);
    setErrorMessage(null);

    try {
      const res = await fetch("http://localhost:8080/flashcard/deck/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          isPublic: isPublic,
          deckName: deckName,
          tagList: nameTags,
        }),
      });

      const json: ApiResponseWithData<string> = await res.json();

      if (!res.ok) {
        setErrorMessage(json.message);
      } else {
        setDeckId(json.data);
        setStep(2);
      }
    } catch (error) {
      setErrorMessage("Failed to send cards to backend: " + error);
    } finally {
      setLoadding(false);
    }
  };

  // ✅ Next → บันทึกการ์ด แล้ว reset ช่อง
  const handleNextCard = () => {
    if (!currentCard.frontCard.trim() || !currentCard.backCard.trim()) {
      setErrorMessage("Please fill in both front and back of the card");
      return;
    }
    setCards([...cards, currentCard]);
    setCurrentCard({ frontCard: "", backCard: "" });
  };

  // ✅ Add → บันทึกการ์ดปัจจุบัน + ไป step 3
  const handleFinishCards = () => {
    if (!currentCard.frontCard.trim() || !currentCard.backCard.trim()) {
      setErrorMessage("Please fill in both front and back of the card");
      return;
    }
    setCards([...cards, currentCard]);
    setCurrentCard({ frontCard: "", backCard: "" });
    setStep(3);
  };

  // ✅ ลบการ์ด
  const removeCard = (index: number) => {
    setCards(cards.filter((_, i) => i !== index));
  };

  // ✅ ส่งข้อมูลจริง (mock)
  const handleSubmitFinal = async () => {
    if (!deckName.trim() || cards.length === 0) {
      setErrorMessage("Deck must have a name and at least one card");
      return;
    }

    setLoadding(true);
    setErrorMessage(null);

    const newDeck: TypeDeckCreate = { deckId: deckId, results: cards };

    try {
      const res = await fetch(
        "http://localhost:8080/flashcard/deck/create_card/",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify(newDeck),
        }
      );

      const json: ApiResponse = await res.json();

      if (!res.ok) {
        setErrorMessage(json.message);
      } else {
        navigate("/inventory");
      }
    } catch (error) {
      setErrorMessage("Failed to send cards to backend: " + error);
    } finally {
      setLoadding(false);
    }
  };

  if (loadding === true) {
    return <h3>Loadding...</h3>;
  }

  if (errorMessage !== null) {
    return <h3>{errorMessage}</h3>;
  }

  return (
    <div className={style.deck_create_container}>
      {step === 1 && (
        <div id="deck_step_1" className={style.deck_create_step1}>
          <h2>Create Deck</h2>

          <div className={style.deck_is_public}>
            <label>IsPublic:</label>
            <div className={style.is_public_content}>
              <div>
                <input
                  type="radio"
                  id="public"
                  name="isPublic"
                  checked={isPublic === true}
                  onChange={() => setIsPublic(true)}
                />
                <label htmlFor="public">Public</label>
              </div>
              <div>
                <input
                  type="radio"
                  id="private"
                  name="isPublic"
                  checked={isPublic === false}
                  onChange={() => setIsPublic(false)}
                />
                <label htmlFor="private">Private</label>
              </div>
            </div>
          </div>

          <div className={style.deck_name}>
            <label>Deck Name:</label>
            <input
              id="deck_name"
              type="text"
              value={deckName}
              onChange={(e) => setDeckName(e.target.value)}
            />
          </div>

          <div className={style.name_tags}>
            <label>Name Tags:</label>
            <input
              id="tag_name"
              type="text"
              value={tagInput}
              onChange={(e) => setTagInput(e.target.value)}
            />
          </div>

          <div className={style.add_tags_container}>
            <div className={style.add_tag_btn}>
              <button id="btn_add_tag" type="button" onClick={addTag}>
                Add Tag
              </button>
            </div>

            <div className={style.show_name_tages}>
              {nameTags.map((tag, index) => (
                <span key={index} className={style.remove_name_tags_btn}>
                  {tag}{" "}
                  <button type="button" onClick={() => removeTag(index)}>
                    ❌
                  </button>
                </span>
              ))}
            </div>
          </div>

          <div className={style.create_deck_btn}>
            <button
              id="btn_create_deck"
              type="button"
              onClick={handleCreateDeck}
            >
              Create Deck
            </button>
          </div>
        </div>
      )}

      {step === 2 && (
        <div id="deck_step_2" className={style.deck_create_step2}>
          <h2>Add Cards</h2>
          <div className={style.create_card}>
            <div className={style.front_card}>
              <label>FrontCard</label>
              <textarea
                id="front_card"
                placeholder="Front Card"
                value={currentCard.frontCard}
                onChange={(e) =>
                  setCurrentCard({ ...currentCard, frontCard: e.target.value })
                }
              />
            </div>
            <div className={style.back_card}>
              <label>BackCard</label>
              <textarea
                id="back_card"
                placeholder="Back Card"
                value={currentCard.backCard}
                onChange={(e) =>
                  setCurrentCard({ ...currentCard, backCard: e.target.value })
                }
              />
            </div>
          </div>

          <div className={style.step2_btn}>
            <button type="button" onClick={handleNextCard}>
              Next ➡
            </button>
            <button id="btn_finish" type="button" onClick={handleFinishCards}>
              ✅ Add & Finish
            </button>
          </div>
        </div>
      )}

      {step === 3 && (
        <div id="deck_step_3" className={style.deck_create_step3}>
          <div className={style.step3_header}>
            <h2>Deck Summary</h2>
            <div className={style.show_deck_is_public}>
              <p>
                <b>IsPublic:</b> {isPublic ? "Public" : "Private"}
              </p>
            </div>
            <div className={style.show_deck_name}>
              <p>
                <b>Deck Name:</b> {deckName}
              </p>
            </div>
            <div className={style.show_tag_list}>
              <p>
                <b>Tags:</b> {nameTags.join(", ")}
              </p>
            </div>
          </div>

          <div className={style.step3_card_content}>
            <p>
              <b>Cards:</b>
            </p>
            <div className={style.card_items}>
              <ol type="1">
                {cards.map((card, index) => (
                  <li key={index}>
                    <div className={style.show_card}>
                      <p className={style.p_front}>
                        <b>Front:</b> {card.frontCard}{" "}
                      </p>
                      <p className={style.p_back}>
                        <b>Back:</b> {card.backCard}{" "}
                      </p>
                      <button
                        className={style.remove_btn}
                        type="button"
                        onClick={() => removeCard(index)}
                      >
                        ❌
                      </button>
                    </div>
                  </li>
                ))}
              </ol>
            </div>
          </div>

          <div className={style.step3_btn}>
            <button type="button" onClick={() => setStep(2)}>
              ⬅ Back
            </button>
            <button
              id="btn_submit_deck"
              type="button"
              onClick={handleSubmitFinal}
            >
              🚀 Submit Deck
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default DeckCreate;
