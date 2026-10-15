package metromind.m6;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/** Randomized/parallel demonstrations for passenger event analytics. */
public final class ScaleAlgorithms {
    private ScaleAlgorithms() {}

    public static void randomizedQuickSort(int[] a) { quickSort(a,0,a.length-1); }
    private static void quickSort(int[] a,int lo,int hi) {
        if(lo>=hi)return;
        int pivotIndex=ThreadLocalRandom.current().nextInt(lo,hi+1);
        int pivot=a[pivotIndex];swap(a,pivotIndex,hi);
        int store=lo;
        for(int i=lo;i<hi;i++)if(a[i]<=pivot)swap(a,store++,i);
        swap(a,store,hi);
        quickSort(a,lo,store-1);quickSort(a,store+1,hi);
    }
    private static void swap(int[]a,int i,int j){int t=a[i];a[i]=a[j];a[j]=t;}

    /** Uniformly samples k records from a stream using Reservoir Sampling. */
    public static <T> List<T> reservoirSample(Iterable<T> stream,int k,Random random) {
        if(k<0)throw new IllegalArgumentException("k cannot be negative.");
        List<T> reservoir=new ArrayList<>(k);int seen=0;
        for(T item:stream){seen++;if(reservoir.size()<k)reservoir.add(item);else if(k>0){int j=random.nextInt(seen);if(j<k)reservoir.set(j,item);}}
        return reservoir;
    }

    public static boolean millerRabin(long n,int rounds) {
        if(n<2)return false;
        for(long p:new long[]{2,3,5,7,11,13,17,19,23,29,31,37}){if(n%p==0)return n==p;}
        long d=n-1;int s=0;while((d&1)==0){d>>=1;s++;}
        Random random=new Random(12345);
        BigInteger N=BigInteger.valueOf(n), Nminus=BigInteger.valueOf(n-1);
        for(int i=0;i<rounds;i++){
            long a=2+Math.floorMod(random.nextLong(),n-3);
            BigInteger x=BigInteger.valueOf(a).modPow(BigInteger.valueOf(d),N);
            if(x.equals(BigInteger.ONE)||x.equals(Nminus))continue;
            boolean witness=true;
            for(int r=1;r<s;r++){x=x.multiply(x).mod(N);if(x.equals(Nminus)){witness=false;break;}}
            if(witness)return false;
        }
        return true;
    }

    public static long parallelPassengerTotal(int[] counts) {
        return IntStream.range(0,counts.length).parallel().mapToLong(i->counts[i]).sum();
    }

    /** Illustrative Brent work/span bound: T_p <= W/P + S (up to constant factors). */
    public static double brentUpperBound(double totalWork,double criticalPath,double processors) {
        if(totalWork<0||criticalPath<0||processors<1)throw new IllegalArgumentException();
        return totalWork/processors+criticalPath;
    }
}
