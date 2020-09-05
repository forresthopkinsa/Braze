export interface DynamicInput {
  name: string;
  key: 'name' | 'slug' | 'author' | 'description' | 'link' | 'donate';
  icon: string;
  value: string | null;
  readonly?: boolean;
}

export interface Expansion {
  index: number;
  slug: string;
  expanded: boolean;
}

export interface BasicElement {
  name: string;
  slug: string;
  author: string;
  description: string;
}
