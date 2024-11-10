  // Configuration variables
  const categories = ["General", "Historia", "Ciencia", "Geografía", "Arte", "Deporte"];
  const colors = ["cat-0", "cat-1", "cat-2", "cat-3", "cat-4", "cat-5"];
  let currentCategory = 0;
  let round = 1;
  let score = [0, 0, 0, 0, 0, 0];
  let currentQuestionIndex = 0;

  // Preguntas de ejemplo
  const questions = [
    { category: 2, question: "¿Cuál es el planeta más cercano al Sol?", options: ["Tierra", "Marte", "Mercurio", "Júpiter"], answer: 2 },
    { category: 1, question: "¿Cuál es este año?", options: ["2020", "2021", "2022", "2023"], answer: 3 },
    { category: 2, question: "¿Cuál es el planeta más cercano al Sol?", options: ["Tierra", "Marte", "Mercurio", "Júpiter"], answer: 2 },
    { category: 1, question: "¿Cuál es este año?", options: ["2020", "2021", "2022", "2023"], answer: 3 },
    { category: 2, question: "¿Cuál es el planeta más cercano al Sol?", options: ["Tierra", "Marte", "Mercurio", "Júpiter"], answer: 2 },
    { category: 1, question: "¿Cuál es este año?", options: ["2020", "2021", "2022", "2023"], answer: 3 },
    // Agrega más preguntas organizadas por categorías
  ];

  // Load año
  document.getElementById("year").innerText = new Date().getFullYear();

  function loadQuestion() {
    // Retrieve current question
    const question = questions[currentQuestionIndex];

    // load current category
    document.getElementById("current-category").innerText = categories[currentCategory];
    
    // Update current category
    currentCategory = question.category;

    // Asigna pregunta a la categoría actual
    document.getElementById("question-text").innerText = question.question;
    document.getElementById("options").innerHTML = "";
    
    // Generate answer option buttons
    question.options.forEach((option, index) => {
      const button = document.createElement("button");
      button.classList.add("btn", "btn-secondary", "mb-2");
      button.innerText = option;
      button.onclick = () => checkAnswer(index);
      document.getElementById("options").appendChild(button);
    });
  }

  function checkAnswer(selectedOption) {
    const question = questions[currentQuestionIndex];
    if (selectedOption === question.answer) {
      score[currentCategory]++;
      updateScore();
      alert("¡Correcto!");
    } else {
      alert("Incorrecto.");
    }
    nextQuestion();
  }

  function updateScore() {
    score.forEach((points, catIndex) => {
      for (let i = 0; i < 3; i++) {
        const box = document.querySelector(`#category-status-${catIndex} .score-box:nth-child(${i + 2})`);
        box.className = "score-box";
        if (i < points) box.classList.add(colors[catIndex]);
        else box.classList.add("unanswered");
      }
    });
  }

  function nextQuestion() {
    currentQuestionIndex++;
    if (score[currentCategory] < 3) {
      loadQuestion();
    } else {
      alert("Categoría completada");
      currentCategory = (currentCategory + 1) % 6;
      loadQuestion();
    }
  }

  loadQuestion();


