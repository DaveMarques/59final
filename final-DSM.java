
String nombre = "Made by David Marques";



int boatsMany = 5;
Boat boats[] = new Boat[boatsMany];

int squadMany = 7;
Squid squad[] = new Squid[squadMany];

int dsmMany = 5;
Lobster dsm[] = new Lobster[dsmMany];

//coord for horizon for Boats
float horizon;
int points;
float lobHiX, lobHiY;
float lobLoX, lobLoY;


void setup() {
  size(500, 500);
  rectMode(CORNERS);
  reset();
}

//reset function, also has starting settings for setup()
void reset() {
  float squadSpace = width/(squadMany+1);
  horizon = height/3;
  points = 0;
  for (int i=0; i<boatsMany; i++ ) {
    boats[i] = new Boat(horizon);
  }
  for (int i=0; i<squadMany; i++ ) {
    squad[i] = new Squid(horizon, squadSpace, i);
  }
  for (int i=0; i<dsmMany; i++ ) {
    dsm[i] = new Lobster(horizon);
  }
  frameCount = 0;
}

void draw() {
  scene();
  if (key =='$' || key == '4') {
    lobReport();
  } else {
    movement();
  }
  display();
}

//scene
void scene() {
  noStroke();
  background(0, 137, 240);//sky
  fill(0);
  text(points, width-50, 20);
  fill(0, 70, 188);
  rect(0, horizon, width, height);//sea
  fill(0);
  text(nombre, 10, height-10);
}

//containing all display methods
void display() {
  stroke(1);

  //triangles on right side
  float tri = 0;
  fill(255, 0, 0);
  while (tri < height) {
    triangle(width-15, horizon+tri, width-15, horizon+(tri+20), width-30, horizon+(tri+10));
    tri += 40;
  }

  for (int i=0; i<boatsMany; i++ ) {
    boats[i].disp(i+1); //display boats
  }

  for (int i=0; i<squadMany; i++) {
    squad[i].disp(); //display squids
  }

  for (int i=0; i<dsmMany; i++) {
    dsm[i].disp(); //display lobsters
  }
}

//containing all movement values
void movement() {

  for (int i=0; i<boatsMany; i++ ) {
    boats[i].move();//boat movement
  }

  for (int i=0; i<squadMany; i++) {
    squad[i].move();//squid movement
    for (int j=0; j<dsmMany; j++) {
      squad[i].hitSquad(dsm[j]);//squid hitting lobster
    }
  }

  for (int i=0; i<dsmMany; i++) {
    dsm[i].move();//lobster moving
  }
}

//determining the highest and lowest Y values
void lobHi() {
  int k=0;
  for (int i=1; i<dsmMany; i++ ) {
    if (dsm[i].posY < dsm[k].posY) k=i;           
  }
  lobHiY= dsm[k].posY;
  lobHiX= dsm[k].posX;
}
//low Y value
void lobLo() {
  int k=0;
  for (int i=1; i<dsmMany; i++ ) {
    if (dsm[i].posY > dsm[k].posY) k=i;           
  }
  lobLoY= dsm[k].posY;
  lobLoX= dsm[k].posX;
}

//displaying the values of the highest y lob and lowest y lob
void lobReport(){
  lobLo();
  lobHi();
  text("X",10,20);
  text("Y",10,30);
  
  text("highest",20,10);
  text(lobHiX,20,20);
  text(lobHiY,20,30);
  
  text("lowest",100,10);
  text(lobLoX,100,20);
  text(lobLoY,100,30);
  
}



///////objects





//squid class
class Squid {
  float legs = random(1, 30);
  float posX, posY;
  float movY;
  float r = random(255);
  float g = random(255);
  float b = random(255);
  int i;
  float horizon;

  //starting squid data
  Squid(float horizon, float spacing, int i) {
    this.horizon = horizon;
    this.i = i;
    posX = (i+1) * spacing;
    posY = height;
    movY = random(-4, -2);      

    int(legs);
    int(r);
    int(g);
    int(b);
  }

  //how the squid moves
  void move() {
    posY += movY;
    if (posY <= horizon+20)movY = random(2, 4);
    if (posY >= height)movY = random(-4, -2);
  }

  //how the squid displays
  void disp() {
    fill(r, g, b);
    arc(posX, posY, 40, 40, PI, TWO_PI, OPEN);
    for (int l=1; l<legs; l++) {
      fill(0);
      line( posX, posY, posX, posY+20);
    }
    fill(0);
    text(i+1, posX, posY);
  }

  //squid hit routine
  void hitSquad( Lobster L ) {
    if (dist(posX, posY, L.posX, L.posY) < 30) {
      this.posY = height;
    }
  }
}



//lobster class 
class Lobster {
  float posX, posY;
  float movX, movY;
  float r = random(255);
  float g = random(255);
  float b = random(255);
  float horizon;

  //lobster starting data
  Lobster(float horizon) {
    this.horizon = horizon;
    posX = random(0, width/2);
    posY = random((height/2 + horizon/2), height);
    movX = random(1, 4);
    movY = random(-1, -4);

    int(r);
    int(g);
    int(b);
  }

  //how the lobster moves
  void move() {
    posY += movY;
    posX += movX;

    //bounce off "walls"
    if (posY < horizon+20) movY = random(2, 4);
    if (posY > height)     movY = random(-4, -2);
    if (posX < 0)          movX = random(2, 4);
    if (posX > width)      movX = random(-4, -2);
  }

  //displaying lobster
  void disp() {

    stroke(0);
    fill(r, g, b);                  //body
    strokeWeight(1);
    ellipse( posX, posY, 40, 20 ); 

    strokeWeight(5);
    stroke(r, g, b);  //leg color

    //displaying lobster legs of lobster moving right
    if (movX > 0) {                                          
      //animation
      if (frameCount % 60 > 30) {
        line(posX+20, posY+1, posX+25, posY+6);
        line(posX+20, posY-1, posX+25, posY-6);
      } else {
        line(posX+20, posY+1, posX+20, posY+10);
        line(posX+20, posY-1, posX+20, posY-10);
      }

      line(posX+10, posY+10, posX+15, posY+15);
      line(posX+10, posY-10, posX+15, posY-15);

      line(posX+0, posY+10, posX+5, posY+15);
      line(posX+0, posY-10, posX+5, posY-15);  

      line(posX-10, posY+10, posX-5, posY+15);
      line(posX-10, posY-10, posX-5, posY-15);
    }

    //displaying lobster legs of lobster moving left
    if (movX < 0) {                                          
      //animation
      if (frameCount % 60 > 30) {
        line(posX-20, posY+1, posX-25, posY+6);
        line(posX-20, posY-1, posX-25, posY-6);
      } else {
        line(posX-20, posY+1, posX-20, posY+10);
        line(posX-20, posY-1, posX-20, posY-10);
      }

      line(posX-10, posY+10, posX-15, posY+15);
      line(posX-10, posY-10, posX-15, posY-15);

      line(posX-0, posY+10, posX-5, posY+15);
      line(posX-0, posY-10, posX-5, posY-15);  

      line(posX+10, posY+10, posX+5, posY+15);
      line(posX+10, posY-10, posX+5, posY-15);
    }
    strokeWeight(1);
  }

  //hit routine
  boolean hit(float x, float y) {
    if (dist(x, y, posX, posY) < 30) {
      return true;
    } else {
      return false;
    }
  }
}



//Boat class
class Boat {
  float posX, posY;
  float movX;
  float r = random(255);
  float g = random(255);
  float b = random(255);
  int fins=0;

  //starting Boat data
  Boat(float horizon) {
    posX = random(0, width);
    posY = horizon;
    movX = random(-4, 4);
    int(r);
    int(g);
    int(b);
  }

  //displaying Boat
  void disp(int i) {
    fill(r, g, b);
    quad(posX-20, posY-20, posX+20, posY-20, posX+10, posY, posX-10, posY);
    triangle(posX-10, posY-20, posX, posY-30, posX+10, posY-20);
    fill(0);

    //"smoke" changing directions depending on movement
    if (this.movX<0) {                                             //-
      if (this.posX % 80 < 60) ellipse(posX, posY-40, 10, 10);     //-
      if (this.posX % 80 < 40) ellipse (posX+5, posY-45, 10, 10);  //-DX    
      if (this.posX % 80 < 20) ellipse (posX+15, posY-50, 10, 10); //-
    } else { 
      if (this.posX % 80 > 60) ellipse(posX-15, posY-50, 10, 10);  //+
      if (this.posX % 80 > 40) ellipse(posX-5, posY-45, 10, 10);   //+
      if (this.posX % 80 > 20) ellipse(posX, posY-40, 10, 10);     //+DY
    }

    //# of Boat
    text(i, posX-3, posY-5);
  }

  //moving Boat
  void move() {
    posX += movX;
    if (posX < 0) {
      movX = random(1.5, 4);
      //score(fins);
    }
    if (posX > width) movX = random(-1.5, -4);
  }
}

/*
//how the boats score
 void score(int s) {
 points+=s;
 }
 */



void mousePressed() {
  reset();
}
