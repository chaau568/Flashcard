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

    return () => {
      document.body.classList.remove(style.inventory_page);
    };
  }, []);

  function visitDeck(id: string, name: string) {
    console.log("Deck id: " + id);
    navigate(`/deck_owner/${id}`, { state: { deckName: name } });
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
            onClick={() => visitDeck(flashcard.id, flashcard.deckName)}
          >
            <h3>{flashcard.deckName}</h3>
          </div>
        ))
      )}
    </div>
  );
};

export default Inventory;
