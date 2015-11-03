/*
 * EODが来るまで入力を受け取り続け、その内容をtest.txtに保存するAI
 */
#include <iostream>
#include <random>
#include <fstream>

using namespace std;

const string commands = "URDLA";
int board[40][40];
int life[4];
int turn;
int x[4];
int y[4];
string dir[4];

int main() {
	ofstream outputfile;
	outputfile.open("test.txt");
	cout << "Ready" << endl;
	random_device rnd;
	while (true) {
		string eod;
		while (true) {
			cin >> eod;
			outputfile << eod << " ";
			if (eod == "EOD") {
				outputfile << endl;
				break;
			}
		}

		int c = rnd() % commands.size();
		cout << commands[c] << endl;
	}

	return 0;
}
