export interface TypeCard {
  id: string;
  ownerDeckId: string;
  frontCard: string;
  backCard: string;
  state: string;
  progress: number;
  processingTime?: string;
  createdAt?: string;
  updatedAt?: string;
}
