#include <iostream>
#include <random>
using namespace std;

const string commands = "URDLA";
int board[40][40];
int life[4];
int turn;
int x[4];
int y[4];
string dir[4];

void action() {}

int main() {
  cout << "READY" << endl;
  random_device rnd;
  while (true) {
    int my_num;
    cin >> my_num;

    cin >> turn;

    for (int i = 0; i < 4; ++i) {
      cin >> life[i];
    }
    for (int i = 0; i < 40; ++i)
      for (int j = 0; j < 40; ++j) cin >> board[i][j];

    for (int i = 0; i < 4; ++i) cin >> y[i] >> x[i];

    string eod;
    cin >> eod;

    int c = rnd() % commands.size();
    cout << commands[c] << endl;
  }

  return 0;
}
