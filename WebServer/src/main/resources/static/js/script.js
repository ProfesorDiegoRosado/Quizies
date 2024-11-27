  // Variables
  let categories = [];
  //const colors = ["cat-0", "cat-1", "cat-2", "cat-3", "cat-4", "cat-5"];
  let colorsByCategory = {};
  let currentCategory = "";
  let round = 0;
  let score = {};

  // Load año
  document.getElementById("year").innerText = new Date().getFullYear();

  function loadQuestion(question) {
    // Update current category
    currentCategory = question.category.name;
    document.getElementById("current-category").innerText = currentCategory;

    // set question
    document.getElementById("question-text").innerText = question.question;
    document.getElementById("options").innerHTML = "";
    
    // Generate answer option buttons
    question.answers.forEach((answer, index) => {
      const button = document.createElement("button");
      button.classList.add("btn", "btn-secondary", "mb-2");
      button.innerText = answer;
      button.onclick = () => checkAnswer(index, question);
      document.getElementById("options").appendChild(button);
    });
  }

  function checkAnswer(selectedOption, question) {
    if (selectedOption === question.rightAnswer) {
      score[currentCategory]++;
      updateScore();
      alert("¡Correcto!");
    } else {
      alert("Incorrecto.");
    }
    nextQuestionServer();
  }

  function updateScore() {
    Object.keys(score).forEach(categoryName => {
      points = score[categoryName];
      catIndex = getIndexOfCategory(categoryName);
      for (let i = 0; i < 3; i++) {
        const box = document.querySelector(`#category-status-${catIndex} .score-box:nth-child(${i + 2})`);
        box.className = "score-box";
        if (i < points) {
          box.style.backgroundColor = colorsByCategory[categoryName];
        } else {
          box.classList.add("unanswered");
        }
      }
    });

  }

  function getIndexOfCategory(categoryName) {
    let categoriesNames = categories.map( function(c) {
      return c['name'];
    })
    catIndex = categoriesNames.indexOf(categoryName);
    return catIndex;
  }

  function updateCategory(categories) {
    categories.forEach( (category, index) => {
      // populate score category dict
      score[category.name] = 0;
      // show categories
      $( ".category-title.cat-"+index).text(category.name);
      // set color
      $( ".category-title.cat-"+index).css('background-color',category.color.hexString);
      colorsByCategory[category.name] = category.color.hexString;
    })
  }

