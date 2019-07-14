// if a user is signed in, we show the form to create a new conversation topic
function showCreateConversationTopicFormIfSignedIn() {
  fetch("/login-status")
    .then(response => {
      return response.json();
    })
    .then(loginStatus => {
      if (loginStatus.isLoggedIn) {
        const createConverstionTopicForm = document.getElementById(
          "create-conversation-topic-div"
        );
        createConverstionTopicForm.classList.remove("hidden");

        const signInToCreateChatroom = document.getElementById(
          "sign-in-to-create-chatroom"
        );
        signInToCreateChatroom.classList.add("hidden");
      }
    });
}

// fetch conversation topics and add them to the page
function fetchConversationTopics() {
  const url = "/conversation-topics";
  fetch(url)
    .then(response => {
      console.log(response);
      return response.json();
    })
    .then(conversationTopics => {
      console.log(conversationTopics);
      populateConversationTopicsContainer(conversationTopics);
    });
}

//
function populateConversationTopicsContainer(conversationTopics) {
  const container = document.getElementById("conversation-topics-container");

  if (!conversationTopics || conversationTopics.length < 1) {
    console.log("conversation topics array is null, undefined or empty");
    container.innerHTML =
      "There are no available conversation topics right now. \
        Go ahead and create one!";
  } else {
    console.log("we have conversation topics");
    container.innerHTML = "";
  }

  conversationTopics.forEach(conversationTopic => {
    const conversationTopicDiv = buildConversationTopicDiv(conversationTopic);
    container.appendChild(conversationTopicDiv);
  });
}

/**
 *
 * @param {ConversationTopic} conversationTopic
 */
function buildConversationTopicDiv(conversationTopic) {
  console.log("building individual conversation topic div");
  const conversationTopicDiv = document.createElement("div");
  conversationTopicDiv.classList.add("conversation-topic-div");

  const aTag = document.createElement("a");
  aTag.setAttribute(
    "href",
    "/single-conversation-topic.html?id=" +
      conversationTopic.id +
      "&title=" +
      conversationTopic.title
  );
  // aTag.setAttribute('href', window.location.href + '?id=' + conversationTopic.id);

  console.log(conversationTopic.title);
  const title = document.createElement("div");
  title.classList.add("conversation-topic-title");
  title.appendChild(document.createTextNode(conversationTopic.title));

  const timestampDiv = document.createElement("div");
  timestampDiv.classList.add("conversation-topic-subtitle");
  console.log(conversationTopic.timestamp);
  // TODO: replace 8hrs ago with function that forms timestamp to x hours ago
  timestampDiv.appendChild(document.createTextNode("8hrs ago"));

  const membersListDiv = document.createElement("div");
  membersListDiv.classList.add("conversation-topic-subtitle");
  membersListDiv.innerHTML = "// list of members in this chatroom";

  const conversationTopicHeader = document.createElement("div");
  conversationTopicHeader.classList.add("conversation-topic-header");
  conversationTopicHeader.appendChild(title);
  conversationTopicHeader.appendChild(timestampDiv);

  aTag.appendChild(conversationTopicHeader);
  aTag.appendChild(membersListDiv);

  conversationTopicDiv.appendChild(aTag);

  return conversationTopicDiv;
}

function formatTime(timeCreated) {
  var diff = Date.now() - timeCreated;

  if (diff > periods.month) {
    // it was at least a month ago
    return Math.floor(diff / periods.month) + "m";
  } else if (diff > periods.week) {
    return Math.floor(diff / periods.week) + "w";
  } else if (diff > periods.day) {
    return Math.floor(diff / periods.day) + "d";
  } else if (diff > periods.hour) {
    return Math.floor(diff / periods.hour) + "h";
  } else if (diff > periods.minute) {
    return Math.floor(diff / periods.minute) + "m";
  }
  return "Just now";
}

function buildUI() {
  showCreateConversationTopicFormIfSignedIn();
  fetchConversationTopics();
}
