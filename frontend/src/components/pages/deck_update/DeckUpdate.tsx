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
  const [loading, setLoading] = useState(true);

  // เพิ่ม state ใหม่สำหรับติดตาม index ของการ์ดที่กำลังแสดง
  const [currentCardIndex, setCurrentCardIndex] = useState(0);

  useEffect(() => {
    document.body.classList.add(style.deck_update_page);

    return () => {
      document.body.classList.remove(style.deck_update_page);
    };
  }, []);

  // โหลดข้อมูล Deck
  useEffect(() => {
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

        // โหลด card
        const cardRes = await fetch(
          `http://localhost:8080/flashcard/card/get_by_deck_id/${deckId}`,
          { credentials: "include" }
        );
        const cardJson = await cardRes.json();
        setCards(cardJson.data);
      } catch (err) {
        alert("Failed to load deck info: " + err);
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
    // เมื่อเพิ่มการ์ดใหม่ ให้ตั้งค่า index ไปยังการ์ดใบใหม่ทันที
    setCards([...cards, { frontCard: "", backCard: "" }]);
    setCurrentCardIndex(cards.length);
  };

  const handleSave = async () => {
    try {
      // 1. Update Deck
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

      // 2. แยก Card เดิม vs Card ใหม่
      const existingCards = cards.filter((c) => c.id); // มี id = ของเดิม
      const newCards = cards.filter((c) => !c.id); // ไม่มี id = ของใหม่

      // 2.1 Update card เดิม
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

      // 2.2 Create card ใหม่ (ถ้ามี)
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

      alert("Deck updated successfully!");
      navigate("/inventory");
    } catch (err) {
      alert("Failed to update deck: " + err);
    }
  };

  // 1. เพิ่มฟังก์ชันสำหรับลบการ์ด
  const deleteCard = () => {
    // สร้าง array ใหม่โดยกรองการ์ดที่ไม่ใช่ใบปัจจุบันออก
    const newCards = cards.filter((_, i) => i !== currentCardIndex);
    setCards(newCards);

    // ปรับ index หลังจากลบ
    if (currentCardIndex >= newCards.length && newCards.length > 0) {
      // ถ้าลบการ์ดใบสุดท้าย ให้ย้าย index ไปยังการ์ดใบสุดท้ายใหม่
      setCurrentCardIndex(newCards.length - 1);
    } else if (newCards.length === 0) {
      // ถ้าลบจนไม่มีการ์ดเหลือ ให้ reset index เป็น 0
      setCurrentCardIndex(0);
    }
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div className={style.deck_update_container}>
      <h2>Edit Deck</h2>

      {/* Deck Info */}
      <div className={style.deck_info}>
        <div className={style.deck_name}>
          <label>
            Deck Name:
            <input
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

        <div className={style.deck_tags}>
          <h3>Tags</h3>
          {tagList.map((tag, index) => (
            <div key={index}>
              <input
                type="text"
                value={tag}
                onChange={(e) => {
                  const newTags = [...tagList];
                  newTags[index] = e.target.value;
                  setTagList(newTags);
                }}
              />
              <button
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
          <button type="button" onClick={() => setTagList([...tagList, ""])}>
            + Add Tag
          </button>
        </div>
      </div>

      <hr />

      {/* Cards - แสดงทีละใบ */}
      <div className={style.deck_card}>
        <h3>
          Card {currentCardIndex + 1} of {cards.length}
        </h3>
        {cards.length > 0 && (
          <div key={cards[currentCardIndex].id || currentCardIndex}>
            <label>
              Front:
              <textarea
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
          onClick={() =>
            setCurrentCardIndex((prevIndex) => Math.max(0, prevIndex - 1))
          }
          disabled={currentCardIndex === 0}
        >
          Previous
        </button>
        <button
          onClick={() =>
            setCurrentCardIndex((prevIndex) =>
              Math.min(cards.length - 1, prevIndex + 1)
            )
          }
          disabled={currentCardIndex === cards.length - 1}
        >
          Next
        </button>
        {/* 2. เพิ่มปุ่ม "Delete Card" */}
        <button onClick={deleteCard} disabled={cards.length === 0}>
          ❌ Delete Card
        </button>
        <button onClick={addCard}>+ Add Card</button>
        <button onClick={handleSave}>Save</button>
      </div>
    </div>
  );
};

export default DeckUpdate;
