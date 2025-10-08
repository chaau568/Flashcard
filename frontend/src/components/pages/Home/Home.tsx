import { useEffect, useState } from "react";
import type { TypeDeck } from "../type/TypeDeck";
import { useNavigate } from "react-router-dom";
import style from "./Home.module.css";

interface ApiResponseWithData<T> {
  message: string;
  status: number;
  data: T;
}

const Home = () => {
  const [flashcards, setFlashcards] = useState<TypeDeck[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add(style.home_page);

    const fetchFlashcards = async () => {
      try {
        const response = await fetch(
          "http://localhost:8080/flashcard/deck/get_by_public",
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
        setErrorMessage("Failed to fetch decks: " + error);
      } finally {
        setLoading(false);
      }
    };

    fetchFlashcards();

    return () => {
      document.body.classList.remove(style.home_page);
    };
  }, []);

  function visitDeck(id: string, name: string) {
    console.log("Deck id: " + id);
    navigate(`/deck_public/${id}`, { state: { deckName: name } });
  }

  return (
    <div className={style.home_container}>
      {loading ? (
        <p id="loading-decks">Loading...</p>
      ) : errorMessage ? (
        <p id="error-message">{errorMessage}</p>
      ) : flashcards.length === 0 ? (
        <p id="no-decks-found">No decks found.</p>
      ) : (
        <div id="deck-list-container" className={style.deck_list_container}>
          {flashcards.map((flashcard) => (
            <div
              key={flashcard.id}
              className={style.flashcard}
              onClick={() => visitDeck(flashcard.id, flashcard.deckName)}
            >
              <h3>{flashcard.deckName}</h3>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Home;
