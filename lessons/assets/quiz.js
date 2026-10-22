"use strict";

document.querySelectorAll("form.quiz").forEach((form) => {
  const feedback = form.querySelector(".feedback");
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const answer = form.querySelector("input:checked");
    if (!answer) return;
    const correct = answer.value === form.dataset.answer;
    feedback.dataset.result = correct ? "correct" : "incorrect";
    feedback.textContent = (correct ? "正确。" : "再想一想。") + answer.dataset.feedback;
  });
  form.addEventListener("change", () => {
    feedback.textContent = "";
    delete feedback.dataset.result;
  });
});

// Print the answer rubrics too, then restore the reader's disclosure state.
const printDetails = new Map();
window.addEventListener("beforeprint", () => {
  document.querySelectorAll("details").forEach((detail) => {
    if (!printDetails.has(detail)) printDetails.set(detail, detail.open);
    detail.open = true;
  });
});
window.addEventListener("afterprint", () => {
  printDetails.forEach((open, detail) => { detail.open = open; });
  printDetails.clear();
});
