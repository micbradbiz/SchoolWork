#ifndef PROCCARD_H
#define PROCCARD_H

//Note: I didn't want to call this struct something like "process" because I feel like
//that would end up somewhere down the road causing name clashes (or just general unneccesary confusion
//and since this is info regarding a process not the process itself i've called this a "card"
//(I could've gone with "profile" but that's too long a name in my opinion)
struct procCard
{
  int procID;
  int procBurstTime;
  int procPriority;  
};

#endif
