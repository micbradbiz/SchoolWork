#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <cstdlib>
#include <ctime>
#include <algorithm>
#include "procCard.h"


using namespace std;

//Context Switch cost
const int CSWITCH = 3;

//Utilities
string remExt(string fName);                                    //Removes filename extensions
int getPriTotal(const vector<procCard> &readyQueue);            //calculate the priority total of a Queue of processes
int drawTicket(int priTotal);					//Draws a lottery ticket, range 1-priorityTotal
bool compByBurst(const procCard &lh, const procCard &rh);	//Compare process cards by BurstTime, ascending order
bool compByPri(const procCard &lh, const procCard &rh);         //Compare process cards by Priority, descending order 

//Scheduling Algs
void firstComeFirstServed(vector<procCard> readyQueue, string fName);
void shortestJobFirst(vector<procCard> readyQueue, string fName);
void roundRobin(vector<procCard> readyQueue, string fName, int tQuantum);
void lottery(vector<procCard> readyQueue, string fName, int tQuantum);

int main(int argc, char** argv){

  ifstream fin;
  vector<procCard> procCards;
  
  //Handle cmdline args
  if(argc < 2){
    cout << "Usage: Program filename filename2 filename3" << endl
	 << "note: filenames 2 through N are optional." << endl;
  }else{

    //Seeding random
    srand(time(NULL));

    //File processing loop
    for(int i = 1; i < argc; i++){
      fin.open(argv[i]);
      if(fin.fail()){ //If theres a problem with file don't use it
	cout << "There was a problem opening file " << argv[i] << endl;
	continue; 
      }
      //Reading file of procedures into procCards
      string line;
      procCard tmp;
      int j = 0;
      while(getline(fin, line)){
	j++;
	switch(j % 3){
	case 1:
	  tmp.procID = stoi(line);
	  break;
	case 2:
	  tmp.procBurstTime = stoi(line);
	  break;
	case 0:
	  tmp.procPriority = stoi(line);
	  procCards.emplace_back(tmp);
	  break;
	}   
      }

      string fName = remExt(argv[i]);

      firstComeFirstServed(procCards, fName);
      shortestJobFirst(procCards, fName);
      roundRobin(procCards, fName, 20);
      roundRobin(procCards, fName, 40);
      lottery(procCards, fName, 40);

      procCards.clear();
      fin.close();
    }
  }
  
  return 0; 
}








//Utilities

//Removes extension from filename
string remExt(string fName){
  return fName.substr(0, fName.find_last_of("."));
}

int getPriTotal(const vector<procCard> &readyQueue){
  int priTotal = 0;
  for(vector<procCard>::const_iterator it = readyQueue.begin(); it < readyQueue.end(); it++){
    priTotal += (*it).procPriority;
  }
  return priTotal;
}

int drawTicket(int priTotal){
  return ((rand() % priTotal) + 1); // range [1-priTotal]
}

bool compByBurst(const procCard &lh, const procCard &rh){
  return (lh.procBurstTime < rh.procBurstTime); //Ascending order
}

bool compByPri(const procCard &lh, const procCard &rh){
  return (lh.procPriority > rh.procPriority); //Descending order
}

//Calculates totalTurnaround and average Turnaround and outputs it to fileout
void calcTurnaround(const vector<int> &completionTimes, ofstream &fout){
  int totTurnaround = 0;
  float avTurnaround = 0.0;
  for(vector<int>::const_iterator it = completionTimes.begin(); it < completionTimes.end(); it++){
    totTurnaround += *it;
  }
  avTurnaround = (float) totTurnaround / completionTimes.size();
  fout << ",,,Total Turnaround:," << totTurnaround << endl
       << ",,,Average Turnaround:," << avTurnaround << endl;
}







//Scheduling Algs

void firstComeFirstServed(vector<procCard> readyQueue, string fName){
  string ofName = "firstcomefirstserved-" + fName + ".csv";
  ofstream fout(ofName);
  vector<int> completionTimes;
  int cpuTime = 0;

  //Header line
  fout << "CpuTime,PID,StartingBurstTime,EndingBurstTime,CompletionTime" << endl;
  //Data lines
  for(vector<procCard>::iterator it = readyQueue.begin(); it < readyQueue.end(); it++){
    fout << cpuTime << "," << (*it).procID << "," << (*it).procBurstTime << ","
	 << 0 << ",";
    completionTimes.emplace_back(cpuTime + (*it).procBurstTime);
    fout << completionTimes.back() << endl;
    cpuTime = completionTimes.back() + CSWITCH;
  }
  
  calcTurnaround(completionTimes, fout);
  
  fout.close();
}




void shortestJobFirst(vector<procCard> readyQueue, string fName){
  string ofName = "shortestjobfirst-" + fName + ".csv";
  ofstream fout(ofName);
  vector<int> completionTimes;
  int cpuTime = 0;

  //Sort readyQueue by Burst time in ascending order
  sort(readyQueue.begin(), readyQueue.end(), compByBurst);
  
  //Header line
  fout << "CpuTime,PID,StartingBurstTime,EndingBurstTime,CompletionTime" << endl;
  //Data lines
  for(vector<procCard>::iterator it = readyQueue.begin(); it < readyQueue.end(); it++){
    fout << cpuTime << "," << (*it).procID << "," << (*it).procBurstTime << ","
	 << 0 << ",";
    completionTimes.emplace_back(cpuTime + (*it).procBurstTime);
    fout << completionTimes.back() << endl;
    cpuTime = completionTimes.back() + CSWITCH;
  }
  
  calcTurnaround(completionTimes, fout);
  fout.close();
}




void roundRobin(vector<procCard> readyQueue, string fName, int tQuantum){
  string ofName = "roundrobin" + to_string(tQuantum)+ "-" + fName + ".csv";
  ofstream fout(ofName);
  vector<int> completionTimes;
  int cpuTime = 0;
  int sBurst = 0;
  
  //Header line
  fout << "CpuTime,PID,StartingBurstTime,EndingBurstTime,CompletionTime" << endl;
  //Data lines
  while(!readyQueue.empty()){
    for(vector<procCard>::iterator it = readyQueue.begin(); it < readyQueue.end(); it++){
      sBurst = (*it).procBurstTime;
      if(sBurst - tQuantum > 0){
	fout << cpuTime << "," << (*it).procID << "," << sBurst << ","
	     << (sBurst - tQuantum) << "," << 0 << endl;
	cpuTime += CSWITCH + tQuantum;
	(*it).procBurstTime -= tQuantum;
      }else{ 
	fout << cpuTime << "," << (*it).procID << "," << sBurst << ","
	     << 0 << ",";
	completionTimes.emplace_back(cpuTime + sBurst);
	fout << completionTimes.back() << endl;
	cpuTime = completionTimes.back() + CSWITCH;
	
	//Deleting here requires we step the iterator back one before deleting otherwise we would invalidate
	//the iterator
	it--;
	readyQueue.erase(it + 1);
      }
    }
  } 
  calcTurnaround(completionTimes, fout);
  fout.close();
}





void lottery(vector<procCard> readyQueue, string fName, int tQuantum){
  string ofName = "lottery" + to_string(tQuantum)+ "-" + fName + ".csv";
  ofstream fout(ofName);
  vector<int> completionTimes;
  int cpuTime = 0;
  int sBurst = 0;

  
  //Header line
  fout << "CpuTime,PID,StartingBurstTime,EndingBurstTime,CompletionTime" << endl;
  //Data lines

  sort(readyQueue.begin(), readyQueue.end(), compByPri);
  int priTotal = getPriTotal(readyQueue);
  int ticket = 0;
  int curPri;
  vector<procCard>::iterator it;
  while(!readyQueue.empty()){
    curPri = priTotal;
    it = readyQueue.begin();
    ticket = drawTicket(priTotal);

    while(ticket <= curPri - (*it).procPriority){
      curPri -= (*it).procPriority;
      it++;
    }
    
    sBurst = (*it).procBurstTime;
    if(sBurst - tQuantum > 0){
      fout << cpuTime << "," << (*it).procID << "," << sBurst << ","
	   << (sBurst - tQuantum) << "," << 0 << endl;
      cpuTime += CSWITCH + tQuantum;
      (*it).procBurstTime -= tQuantum;
    }else{
      fout << cpuTime << "," << (*it).procID << "," << sBurst << ","
	   << 0 << ",";
      completionTimes.emplace_back(cpuTime + sBurst);
      fout << completionTimes.back() << endl;
      cpuTime = completionTimes.back() + CSWITCH;

      readyQueue.erase(it);
      priTotal = getPriTotal(readyQueue);
    } 
  } 
  calcTurnaround(completionTimes, fout);
  fout.close();
}

