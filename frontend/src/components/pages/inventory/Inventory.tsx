import { useEffect, useState } from "react";
import type { TypeDeck } from "../type/TypeDeck";
import { useNavigate } from "react-router-dom";
import style from "./Inventory.module.css";

interface ApiResponseWithData<T> {
  message: string;
  status: number;
  data: T;
}

const Inventory = () => {
  const [flashcards, setFlashcards] = useState<TypeDeck[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const [visitDeck, setVisitDeck] = useState<boolean>(false);
  const [addNewCard, setAddNewCard] = useState<boolean>(false);
  const [deckIdSelect, setDeckIdSelect] = useState<string>("");
  const [deckNameSelect, setDeckNameSelect] = useState<string>("");

  useEffect(() => {
    document.body.classList.add(style.inventory_page);

    const fetchFlashcards = async () => {
      try {
        const response = await fetch(
          "http://localhost:8080/flashcard/deck/get_by_owner_user_id",
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            credentials: "include", // ✅ ส่ง session/cookie ไปด้วย
          }
        );

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const json: ApiResponseWithData<TypeDeck[]> = await response.json();
        setFlashcards(json.data); // ✅ ต้องเอาจาก json.data
      } catch (error) {
        alert("Failed to fetch decks:" + error);
      } finally {
        setLoading(false);
      }
    };

    fetchFlashcards();

    if (visitDeck) {
      navigate(`/deck_owner/${deckIdSelect}`, {
        state: { deckName: deckNameSelect },
      });
    }

    if (addNewCard) {
      navigate("/deck_create");
    }

    return () => {
      document.body.classList.remove(style.inventory_page);
    };
  }, [visitDeck, addNewCard]);

  function deckSelect(id: string, name: string) {
    setDeckIdSelect(id);
    setDeckNameSelect(name);
    setVisitDeck(true);
  }

  return (
    <div className={style.inventory_container}>
      {loading ? (
        <p>Loading...</p>
      ) : flashcards.length === 0 ? (
        <p>No decks found.</p>
      ) : (
        flashcards.map((flashcard) => (
          <div
            key={flashcard.id}
            className={style.flashcard}
            onClick={() => deckSelect(flashcard.id, flashcard.deckName)}
          >
            <h3>{flashcard.deckName}</h3>
          </div>
        ))
      )}

      <div className={style.addNewCardContainer}>
        <button
          onClick={() => setAddNewCard(true)}
          className={style.addNewCardButton}
        >
          New Flashcard
        </button>
      </div>
    </div>
  );
};

export default Inventory;
