// get query param
// when fetching messages, pass that query param as a query param

const urlParams = new URLSearchParams(window.location.search);
const parameterConversationTopicId = urlParams.get("id");
const parameterConversationTopicTitle = urlParams.get("title");

console.log(parameterConversationTopicId);

function setConversationTopicTitle() {
  const url = "/conversation-topics";
  let title = "";
  fetch(url)
    .then(response => {
      return response.json();
    })
    .then(conversationTopics => {
      console.log(conversationTopics);
      conversationTopics.forEach(topic => {
        console.log(topic.title);
        console.log(parameterConversationTopicTitle);
        if (topic.title === parameterConversationTopicTitle) {
          console.log(topic);
          const titleDiv = document.getElementById(
            "conversation-topic-title-div"
          );
          titleDiv.innerHTML = topic.title;
        }
      });
    });
}

/**
 * creates a hidden input element that holds the ConversationTopicId to submit with the form
 */
function createHiddenInputElement() {
  const form = document.getElementById("single-conversation-topic-form");
  console.log(form);
  const input = document.createElement("input");
  input.classList.add("hidden");
  input.name = "conversationTopicId";
  input.value = parameterConversationTopicId;
  console.log(input.value);
  form.appendChild(input);
  console.log(form);
}

/* Fetches the Blobstore URL (where the image will be stored) then displays the form 
then proceed to store new message*/
function fetchBlobstoreUrlAndShowForm() {
  fetch("/blobstore-upload-url")
    .then(response => {
      return response.text();
    })
    .then(imageUploadUrl => {
      console.log("imageUploadUrl is: " + imageUploadUrl);
      const form = document.getElementById("single-conversation-topic-form");
      form.action = imageUploadUrl;
    });
}

function fetchMessages() {
  console.log("single-conversation-topic.js -- fecthing messages");
  const url = "/single-conversation-topic?id=" + parameterConversationTopicId;
  fetch(url)
    .then(response => {
      return response.json();
    })
    .then(messages => {
      console.log(messages);
      const messagesContainer = document.getElementById(
        "conversation-topic-messages-div"
      );
      if (messages.length === 0) {
        messagesContainer.innerHTML =
          "Don't be shy to start the conversation 😬";
      } else {
        messagesContainer.innerHTML = "";
      }
      messages.forEach(msg => {
        const messageDiv = buildMessageDiv(msg);
        messagesContainer.appendChild(messageDiv);
      });
    });
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const wrapper = document.createElement("div");
  wrapper.classList.add("wrapper");
  wrapper.style = "display: flex";

  const profile_div = document.createElement("div");
  profile_div.classList.add("profile-div");
  // add fontawesome pic here
  profile_div.appendChild(document.createTextNode(message.user));
  wrapper.appendChild(profile_div);

  const messageCard = document.createElement("div");
  messageCard.classList.add("message-card", "card");

  const timestampDiv = document.createElement("div");
  timestampDiv.classList.add("timestampDiv", "card-header");
  timestampDiv.appendChild(
    document.createTextNode(new Date(message.timestamp))
  );
  messageCard.appendChild(timestampDiv);

  messageCard.appendChild(document.createElement("hr"));

  const messageDiv = document.createElement("div");
  messageDiv.classList.add("messageDiv", "card-body");
  messageDiv.innerHTML = convertImageAddressToAnchorTag(message.text);
  messageCard.appendChild(messageDiv);

  const imageUrl = message.imageUrl;
  // 1st check checks for null, undefined, empty strings
  // 2nd check check if string is made up  of only white spaces
  if (Boolean(imageUrl) && !!imageUrl.trim()) {
    console.log("inside if");
    const image = document.createElement("img");
    image.src = imageUrl;
    console.log(image);
    messageDiv.appendChild(image);
  }

  wrapper.appendChild(messageCard);
  return wrapper;
}

/** Replace image links with the img HTML tag*/
function convertImageAddressToAnchorTag(text) {
  const regex = /(https?:\/\/.*\.(?:png|jpg))/i;
  const replacement = '<a href="$&" target="_blank">$&</a>';
  const result = text.replace(regex, replacement);
  console.log(result);
  return result;
}

function buildUI() {
  setConversationTopicTitle();
  createHiddenInputElement();
  fetchBlobstoreUrlAndShowForm();
  fetchMessages();
}
