package hu.logcontrol.mobilflexandroid.logger;

/*
 - itt tároljuk a függvényhívások számát egy metódushívás során
 - az alaphelyzet azért 1, mert a legelső függvényhívás minidg az lesz, amelyik sorban meglett hívva a függvény, annak az osztálynak a neve lesz
    -> pl.: ha a MainActivityView -ban hívok egy ApplicationLogger függvényt, akkor a MainActivityView hívófüggvénye fog szerepelni először a Stack Trace listában (a verem alján lesz)
    -> ha a MainActivityView OnCreate metódusában hívom meg, akkor a legelső függvény a Stack Trace-ben az OnCreate függvény lesz
 - mivel a ApplicationLogger csak loggol, ezért a könnyebb mozgathatóság miatt (bármelyik másik programba való áthelyezés) a kezdő függvény hívását már alapból hozzáadom a számlálóhoz
*/

class MethodCallCounter {

    protected static int counter = 1;

    protected static void add(){
        counter += 1;
    }

    protected static void addNumber(int number){ counter += number; }

    protected static void clear(){
        counter = 1;
    }
}
