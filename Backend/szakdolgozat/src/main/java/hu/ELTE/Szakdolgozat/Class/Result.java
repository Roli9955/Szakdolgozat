package hu.ELTE.Szakdolgozat.Class;

public class Result<T, R> {

    private final T res1;
    private final R res2;

    public Result(T res1, R res2){
        this.res1 = res1;
        this.res2 = res2;
    }

    public T getRes1(){
        return this.res1;
    }

    public R getRes2(){
        return this.res2;
    }

}
