package Game;

class Item
{
	int x;
	int y;

	Item(int x, int y){
		this.x = x;
		this.y = y;
	}

	void move(int x, int y)
	{
		this.x += x;
		this.y += y;
	}
}



