#include<stdio.h>
int main()
{
	system("javac DataPreprocess.java");
	system("java DataPreprocess");
	system("javac NRA.java");
	system("java NRA");
	system("rm *.class input");
	return (0);
}
