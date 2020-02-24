//iostream required
#include "mbrad_inpVal.h"

using namespace std;

void getInt(string prompt, int &userInp){
  do{
    cout << prompt;
    cin >> userInp;
    cout << userInp;
    cin.ignore(numeric_limits<streamsize>::max(), '\n');
  }while(!isdigit(userInp));
}
