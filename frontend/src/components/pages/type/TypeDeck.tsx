export interface TypeDeck {
  id: string;
  ownerUserId: string;
  isPublic: boolean;
  deckName: string;
  cardListId: string[];
  tagList: string[];
  createdAt: string;
  updatedAt: string;
}
