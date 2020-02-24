// Formal Language and Automata, CS 311, 
// Name: Bradford, Michael
// Project: Deteministic Pushdown Automaton
// Due: 6/6/2018 Midnight
// Dpda project

#include <iostream>
#include <iomanip>
#include <limits>
#include <set>
#include <stack>

using namespace std;

struct transition{
  int startState;
  int endState;
  char inputRead;
  char stackRead;
  string pushStack;
  transition(){
  }
  transition(int start, int end, char inp, char sRead, string pushS){
    startState = start;
    endState = end;
    inputRead = inp;
    sRead = stackRead;
    pushStack = pushS;    
  }
};

//Compare function just to make sure when entering a transition into the set that it isn't the same transition
bool operator<(const transition &lhs, const transition &rhs){
  return !( (lhs.startState == rhs.startState) && (lhs.inputRead == rhs.inputRead)
	   && (lhs.stackRead == rhs.stackRead) && (lhs.pushStack == rhs.pushStack) && (lhs.endState == rhs.endState) ); 
}


struct dpda{
  int totalStates;
  set<char> alphabet;
  set<transition> transitions;
  set<int> finalStates;
  stack<char> dStack;
};

void getTransition(transition &t, bool &success, bool &done);
void createDpda(dpda &d);
void getInt(string prompt, int &i);
void showStack(stack<char> s);
bool checkForTransition(int curState, int lastState, char uInp, char topOfStack, const set<transition> &transitions, int &transInd);






//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////






int main(){
  dpda d;
  int curState;
  int transInd;
  bool invalidStep;
  char uInp;
  
  createDpda(d);
  curState = 0;
  d.dStack.push('$');
  invalidStep = false;
  
  while(!invalidStep){
    cout << "Current status " << curState << ":";
    showStack(d.dStack);
    cout <<", Enter Input: ";
    cin >> uInp;
    cin.ignore(numeric_limits<streamsize>::max(), '\n'); //Ignore anything past first char

    if(d.alphabet.find(uInp) != d.alphabet.end() || ((uInp == '.') && (d.dStack.top() == '$'))){
      if(!((uInp == '.') && (d.dStack.top() == '$'))){
	//valid input
	if(checkForTransition(curState, d.totalStates, uInp, d.dStack.top(),d.transitions, transInd)){
	  //Found a transition and it is deterministic in behavior take step
	  transition stepTransition;

	  //Find transition in d.transitions and assign it to stepTransition
	  set<transition>::iterator it = d.transitions.begin();
	  advance(it, transInd);
	  stepTransition = *it;

	  //Take that step
	  curState = stepTransition.endState;
	  //Pop current stack variable. '.' is lambda so don't pop if that's the case.
	  if(stepTransition.stackRead != '.'){
	    d.dStack.pop();
	  }
	  //Push string into stack. '.' is lambda so don't push if that's the case.
	  if(stepTransition.pushStack != "."){
	    for(int i = stepTransition.pushStack.length() - 1; i >= 0; i--){
	      d.dStack.push(stepTransition.pushStack[i]);
	    }
	  }
	}else{
	  //Didnt find a transition or transition was non-deterministic
	  invalidStep = true;
	  if(transInd == -1){
	    invalidStep = true;
	    cout << "No valid Transition found, therefore string is REJECTED" << endl;
	  }else{
	    invalidStep = true;
	    cout << "Non-Deterministic behavior detected, dpda is invalid" << endl;
	  }
	}
      }else{
	//Read lambda with $ only on stack
	if(d.finalStates.find(curState) != d.finalStates.end()){
	  //Found curstate in Final States, no more input, accept
	  d.dStack.pop(); //pop off final $
	  invalidStep = true; 
	  cout << "String ACCEPTED" << endl;
	}else{
	  //No more input, curState isn't final, reject
	  d.dStack.pop();
	  invalidStep = true;
	  cout << "String REJECTED" << endl;
	}
      }
    }else{
      //invalid input, outside alphabet
      invalidStep = true;
      cout << "That is not within the alphabet, therefore string is REJECTED" << endl;
    }    
  }
  
  
  return 0;
}










//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////

//Function defs

void getInt(string prompt, int &i){
  bool validInput = false;
  do{
    cout << prompt;
    if(cin >> i){
      validInput = true;
    }else{
      cout << endl << "bad input" << endl;
      cin.clear();
      cin.ignore(numeric_limits<streamsize>::max(), '\n');      
    }
  }while(!validInput);
}

void getString(string prompt, string &s){
  bool validInput = false;
  do{
    cout << prompt;
    if(cin >> s){
      validInput = true;
    }else{
      cout << endl << "bad input" << endl;
      cin.clear();
      cin.ignore(numeric_limits<streamsize>::max(), '\n');      
    }
  }while(!validInput);
}

void getTransition(transition &t, bool &success, bool &done){
  
  //0 a $ 1 A$
  string trans;
  getline(cin, trans);
  if((isdigit(trans[0])) && (isalnum(trans[2])) && (isprint(trans[4])) && (isdigit(trans[6])) && (!isspace(trans[8]))){
    t.startState = trans[0] - '0';
    t.inputRead = trans[2];
    t.stackRead = trans[4];
    t.endState = trans[6] - '0';
    t.pushStack = trans.substr(8, trans.length());
    success = true;
  }else if(isdigit(trans[1]) && (trans[1] == '1') && (trans[0] == '-')){
    success = false;
    done = true;
  }else{
    success = false;
    done = false;
  }
}

void createDpda(dpda &d){
  int tStates, finS = 0;
  string alphString;
  bool goodTransRead = false, doneTransRead = false;
  transition tmp;
  getInt("Please enter the total # of states:", tStates);
  d.totalStates = tStates;
  getString("Please enter a string of characters representing the alphabet (ex:ab):", alphString);
  alphString = alphString + "."; //lambda should be part of the alphabet automatically
  for(int i = 0; i < alphString.length(); i++){
    d.alphabet.insert(alphString[i]);
  }
  getline(cin, alphString); //Getting rid of '/n' left by cin priming for getline in getTransition.

  cout << "please enter a transition with format \"0 a $ 1 A$\"" << endl
       << "where 0 is starting state, 1 is ending state, a is what is read from input"
       << " $ is the peek at top of the stack, and A$ is a string pushed onto the stack,"
       << " -1 when done"
       << endl;
  do{
    getTransition(tmp, goodTransRead, doneTransRead);
    if(goodTransRead){
      d.transitions.insert(tmp);
    }else if(!doneTransRead){
      cout << "bad transition entered." << endl;
    }
  }while(!doneTransRead);

  cout << "Enter the final states seperated by spaces and delimited by a -1." << endl;
  while(finS != -1){
    getInt("" ,finS);
    if(finS != -1){
      d.finalStates.insert(finS);
    }

  }

}
void showStack(stack<char> s){
  while(!s.empty()){
    cout << s.top();
    s.pop();
  }
}

bool checkForTransition(int curState, int lastState, char uInp, char topOfStack,  const set<transition> &transitions, int &transInd){

  set<transition> subTransitions;
  set<transition>::iterator it = transitions.begin();
  for(int i = 0; i < transitions.size(); i++){
    //If the transition at i's start State is our current State, its InputRead is our user's input and the transitions endState isn't outside of the range of our states than we have a potential transition
    if(it->startState == curState && it->inputRead == uInp && it->stackRead == topOfStack && it->endState <= lastState){
      subTransitions.insert(*it);
      transInd = distance(transitions.begin(), it);
    }
    
    it++;
  }
  if(subTransitions.empty()){
    //There are no potential transitions, Cannot transition Reject string
    transInd = -1;
    return false;
  }else if(subTransitions.size() > 1){
    //There are potential transitions, but there are more than one which shows non-deterministic behavior Reject the dpda itself
    transInd = -2;
    return false;
  }else{
    //There is a potential transition, transInd has been assigned
    return true;
  }
  
}
