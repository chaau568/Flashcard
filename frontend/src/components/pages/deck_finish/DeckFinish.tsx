import "./DeckFinish.css";
import { useLocation, useNavigate } from "react-router-dom";
import type { TypeCardResult } from "../type/TypeCardResult";
import { useState, useEffect } from "react";

const DeckFinish = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [message, setMessage] = useState<string>("");

  const { deckId, deckName, results } = location.state as {
    deckId: string;
    deckName: string;
    results: TypeCardResult[];
  };

  useEffect(() => {
    const updateCards = async () => {
      try {
        const res = await fetch(
          "http://localhost:8080/flashcard/card/update_progress_card",
          {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({ deckId, results }),
          }
        );

        if (!res.ok) {
          throw new Error("Failed to update cards");
        }

        const json = await res.json();
        setMessage(json.message);
      } catch (error) {
        alert("Failed to send cards to backend: " + error);
      }
    };

    if (results && results.length > 0) {
      updateCards();
    }
  }, [results, deckId]);

  if (!results) {
    navigate("/inventory");
    return null;
  }

  return (
    <div className="deck-finish-container">
      <h2>Deck Finished: {deckName}</h2>
      <h3>{message}</h3>
      <button onClick={() => navigate("/inventory")}>Exit</button>
    </div>
  );
};

export default DeckFinish;
