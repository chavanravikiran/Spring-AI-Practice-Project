export interface Summary {
  id?: number;
  originalText: string;
  summarizedText?: string;
  createdAt?: string;
  modelUsed?: string;
}

export interface SummarizeRequest {
  text: string;
}

export interface DeleteResponse {
  message: string;
  status: string;
}

export interface ErrorResponse {
  message: string;
  status: string;
}
