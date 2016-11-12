# AB Number Guessing Game

This is a AB Number Guessing Game AKA well known [Bulls and Cows](https://en.wikipedia.org/wiki/Bulls_and_Cows).
An Old, simple but fun game. <br>
You can fight with your friend to see who can guess the number first! <br>
Hope you enjoy the game <br>


### Rules
- There will be a secret number which is a 4 digits number with four different number from 0 to 9.
- Two player will try to guess the secret number produce by server in turns. Faster one wins.
- If the matching digits are in right position, thet are "Bulls". (Sign "A") <br>
  If in different posistion, they are "cows". (Sign "B") <br>
  
### How to find your opponent
- Enter the oppoent's ID
- The server will help you to find him.

### In the fight mode you can
- Have 1 to 1 battle with your friend any where.
- Brainstorming
- Find new spark of this old game.

### How this app works
Basically the app is sending POST request to query data from [server](https://github.com/JasperHsieh/NumberGameServer) .<br>
There can be devided two state.<br>

##### Pairing state
In this state, the app will register player's ID to server database. After registering player's ID,<br>
the app will keep checking if opponent is registered or not from server. Once it found oppoent is registered, <br>
the game will start immediately.

##### Gaming state
In this state, this app will decide wether the player can submit number since the players should take turns <br>
And of course the most import part is to submit number to server. <br>
It is sent by POST request too. <br>

I wrote this APP just for fun. Feel free to use it!







