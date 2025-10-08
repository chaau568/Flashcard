import { useLocation, useNavigate } from "react-router-dom";
import type { TypeCardResult } from "../type/TypeCardResult";
import { useState, useEffect } from "react";
import type { TypeCard } from "../type/TypeCard";
import style from "./DeckFinish.module.css";

interface ApiResponseWithData<T> {
  message: string;
  status: number;
  data: T;
}

const DeckFinish = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [message, setMessage] = useState<string>("");
  const [stateSummary, setStateSummary] = useState<{ [key: string]: number }>(
    {}
  );

  const { deckId, deckName, results } = location.state as {
    deckId: string;
    deckName: string;
    results: TypeCardResult[];
  };

  useEffect(() => {
    document.body.classList.add(style.deck_finish_page);

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

        const json: ApiResponseWithData<TypeCard[]> = await res.json();

        if (!res.ok) {
          setMessage(json.message);
          // throw new Error(json.message || "Failed to update cards");
        }

        console.table(json.data);

        const summary = json.data.reduce(
          (acc, card) => {
            const key = card.state?.trim().toLowerCase();
            if (key) {
              acc[key] = (acc[key] || 0) + 1;
            }
            return acc;
          },
          {} as { [key: string]: number }
        );

        setStateSummary(summary);
      } catch (error) {
        setMessage("Failed to send cards to backend: " + error);
      }
    };

    if (results && results.length > 0) {
      updateCards();
    }

    return () => {
      document.body.classList.remove(style.deck_finish_page);
    };
  }, [results, deckId]);

  if (!results) {
    navigate("/inventory");
    return null;
  }

  return (
    <div className={style.deck_finish_container}>
      <div className={style.header}>
        <h2>Deck Finished: {deckName}</h2>
        <h3>{message}</h3>
      </div>
      <div className={style.deck_finish_content}>
        <div className={style.summary}>
          <h2>Card States Summary</h2>
        </div>
        <div className={style.summary_new}>
          <h3>New: {stateSummary["new"] || 0}</h3>
        </div>
        <div className={style.summary_learning}>
          <h3>Learning: {stateSummary["learning"] || 0}</h3>
        </div>
        <div className={style.summary_understand}>
          <h3>Understanded: {stateSummary["understanded"] || 0}</h3>
        </div>
      </div>
      <div className={style.btn}>
        <button id="btn-exit" onClick={() => navigate("/inventory")}>
          Exit
        </button>
      </div>
    </div>
  );
};

export default DeckFinish;
