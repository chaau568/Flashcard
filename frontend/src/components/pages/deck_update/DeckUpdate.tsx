import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import style from "./DeckUpdate.module.css";
import type { TypeDeck } from "../type/TypeDeck";

interface TypeCard {
  id?: string;
  frontCard: string;
  backCard: string;
}

interface ApiResponseWithData<T> {
  message: string;
  status: number;
  data: T;
}

const DeckUpdate = () => {
  const { deckId } = useParams<{ deckId: string }>();
  const navigate = useNavigate();

  const [deckName, setDeckName] = useState("");
  const [isPublic, setIsPublic] = useState(false);
  const [tagList, setTagList] = useState<string[]>([]);
  const [cards, setCards] = useState<TypeCard[]>([]);
  const [deletedCardIds, setDeletedCardIds] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const [currentCardIndex, setCurrentCardIndex] = useState(0);

  useEffect(() => {
    document.body.classList.add(style.deck_update_page);

    return () => {
      document.body.classList.remove(style.deck_update_page);
    };
  }, []);

  useEffect(() => {
    setErrorMessage(null);
    const fetchDeck = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/flashcard/deck/get_info/${deckId}`,
          { credentials: "include" }
        );
        const json: ApiResponseWithData<TypeDeck> = await res.json();
        setDeckName(json.data.deckName);
        setIsPublic(json.data.isPublic);
        setTagList(json.data.tagList);

        const cardRes = await fetch(
          `http://localhost:8080/flashcard/card/get_by_deck_id/${deckId}`,
          { credentials: "include" }
        );
        const cardJson = await cardRes.json();
        setCards(cardJson.data);
      } catch (err) {
        setErrorMessage("Failed to load deck info: " + err);
      } finally {
        setLoading(false);
      }
    };

    if (deckId) fetchDeck();
  }, [deckId]);

  const handleCardChange = (
    index: number,
    field: "frontCard" | "backCard",
    value: string
  ) => {
    const newCards = [...cards];
    newCards[index][field] = value;
    setCards(newCards);
  };

  const addCard = () => {
    setCards([...cards, { frontCard: "", backCard: "" }]);
    setCurrentCardIndex(cards.length);
  };

  const handleSave = async () => {
    try {
      await fetch("http://localhost:8080/flashcard/deck/update", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          deckId,
          deck: {
            deckName,
            isPublic,
            tagList,
          },
        }),
      });

      const existingCards = cards.filter((c) => c.id);
      const newCards = cards.filter((c) => !c.id);
      setErrorMessage(null);

      for (const card of existingCards) {
        await fetch("http://localhost:8080/flashcard/card/update", {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({
            ownerDeckId: deckId,
            cardId: card.id,
            card: {
              frontCard: card.frontCard,
              backCard: card.backCard,
            },
          }),
        });
      }

      if (newCards.length > 0) {
        await fetch("http://localhost:8080/flashcard/deck/create_card/", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({
            deckId,
            results: newCards.map((c) => ({
              frontCard: c.frontCard,
              backCard: c.backCard,
            })),
          }),
        });
      }

      if (deletedCardIds.length > 0) {
        await fetch("http://localhost:8080/flashcard/card/delete", {
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({
            ownerDeckId: deckId,
            cardId: deletedCardIds,
          }),
        });
      }

      setErrorMessage("Deck updated successfully!");
      navigate("/inventory");
    } catch (err) {
      setErrorMessage("Failed to update deck: " + err);
    }
  };

  const deleteCard = () => {
    const deletedCard = cards[currentCardIndex];

    if (deletedCard.id) {
      setDeletedCardIds((prev) => [...prev, deletedCard.id!]);
    }

    const newCards = cards.filter((_, i) => i !== currentCardIndex);
    setCards(newCards);

    if (currentCardIndex >= newCards.length && newCards.length > 0) {
      setCurrentCardIndex(newCards.length - 1);
    } else if (newCards.length === 0) {
      setCurrentCardIndex(0);
    }
  };

  if (loading) return <p>Loading...</p>;

  if (errorMessage) return <p>{errorMessage}</p>;

  return (
    <div id="deck-update-container" className={style.deck_update_container}>
      <h2>Edit Deck</h2>
      <div className={style.deck_info}>
        <div className={style.deck_name}>
          <label>
            Deck Name:
            <input
              id="deck-name-input"
              type="text"
              value={deckName}
              onChange={(e) => setDeckName(e.target.value)}
            />
          </label>
        </div>

        <div className={style.deck_is_public}>
          <label>IsPublic:</label>
          <div className={style.is_public_content}>
            <div>
              <input
                type="radio"
                id="public-radio"
                name="isPublic"
                checked={isPublic === true}
                onChange={() => setIsPublic(true)}
              />
              <label htmlFor="public-radio">Public</label>
            </div>
            <div>
              <input
                type="radio"
                id="private-radio"
                name="isPublic"
                checked={isPublic === false}
                onChange={() => setIsPublic(false)}
              />
              <label htmlFor="private-radio">Private</label>
            </div>
          </div>
        </div>

        <div className={style.deck_tags}>
          <h3>Tags</h3>
          {tagList.map((tag, index) => (
            <div key={index}>
              <input
                id={`tag-input-${index}`}
                type="text"
                value={tag}
                onChange={(e) => {
                  const newTags = [...tagList];
                  newTags[index] = e.target.value;
                  setTagList(newTags);
                }}
              />
              <button
                id={`delete-tag-btn-${index}`}
                type="button"
                onClick={() => {
                  const newTags = tagList.filter((_, i) => i !== index);
                  setTagList(newTags);
                }}
              >
                ❌
              </button>
            </div>
          ))}
        </div>
        <div className={style.deck_add_tag}>
          <button
            id="add-tag-btn"
            type="button"
            onClick={() => setTagList([...tagList, ""])}
          >
            + Add Tag
          </button>
        </div>
      </div>

      <hr />

      <div className={style.deck_card}>
        <h3>
          Card {currentCardIndex + 1} of {cards.length}
        </h3>
        {cards.length > 0 && (
          <div key={cards[currentCardIndex].id || currentCardIndex}>
            <label>
              Front:
              <textarea
                id="front-card-input"
                value={cards[currentCardIndex].frontCard}
                onChange={(e) =>
                  handleCardChange(
                    currentCardIndex,
                    "frontCard",
                    e.target.value
                  )
                }
              />
            </label>
            <br />
            <label>
              Back:
              <textarea
                id="back-card-input"
                value={cards[currentCardIndex].backCard}
                onChange={(e) =>
                  handleCardChange(currentCardIndex, "backCard", e.target.value)
                }
              />
            </label>
          </div>
        )}
      </div>

      <div className={style.deck_update_btn}>
        <button
          id="previous-btn"
          onClick={() =>
            setCurrentCardIndex((prevIndex) => Math.max(0, prevIndex - 1))
          }
          disabled={currentCardIndex === 0}
        >
          Previous
        </button>
        <button
          id="next-btn"
          onClick={() =>
            setCurrentCardIndex((prevIndex) =>
              Math.min(cards.length - 1, prevIndex + 1)
            )
          }
          disabled={currentCardIndex === cards.length - 1}
        >
          Next
        </button>
        <button
          id="delete-card-btn"
          onClick={deleteCard}
          disabled={cards.length === 0}
        >
          ❌ Delete Card
        </button>
        <button id="add-card-btn" onClick={addCard}>
          + Add Card
        </button>
        <button id="save-btn" onClick={handleSave}>
          Save
        </button>
      </div>
    </div>
  );
};

export default DeckUpdate;
