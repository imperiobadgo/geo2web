import {ConstructionElementRead} from "./construction-element-read";
import {ConstructionElementWrite} from "./construction-element-write";

export class ConstructionElement {
  id: string;

  name: string;

  input: string;

  output: string;

  transform: number[];

  constructor(read: ConstructionElementRead) {
    this.id = read.id;
    this.name = read.name;
    this.input = read.input;
    this.output = read.output;
    this.transform = read.transform;
  }

  public createWriteConstructionElement(): ConstructionElementWrite {
    return {
      id: this.id,
      name: this.name,
      input: this.input,
      transform: this.transform,
    };
  }
}
