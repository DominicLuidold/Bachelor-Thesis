customElements.define('word-count', WordCount, { extends: 'p' });

[...]

class WordCount extends HTMLParagraphElement {
  constructor() {
    // Always call super first in constructor
    super();

    // Element functionality written in here

    ...
  }
}
