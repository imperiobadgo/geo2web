import {ConstructionElementRead} from "./construction-element-read";

export const emptyConstructionElementRead = (): ConstructionElementRead => ({
  id: "",
  constructionIndex: 0,
  name: "",
  input: "",
  output: "",
  transform: []
});
