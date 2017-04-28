/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package threadPack;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author Juliano
 */
public class StringThread implements Runnable {
    private static AtomicReference<StringBuffer> strFinal = new AtomicReference<>();
    static AtomicInteger atomInt = new AtomicInteger(0);
    static BlockingQueue<StringBuffer> fila = new LinkedBlockingQueue<>();
    
    @Override
    public void run() {
        try {   
            do {
                if(fila.peek() != null) {
                    strFinal.set(fila.take());

                    System.out.println(Thread.currentThread().getName()+"\t"+strFinal);
                    strFinal.get().setCharAt(atomInt.get(), (char)(strFinal.get().charAt(atomInt.getAndIncrement())-32));
                    fila.put(strFinal.get());
                    Thread.sleep(1000);                    
                }
            } while(atomInt.get() <= strFinal.get().length());
        } catch (StringIndexOutOfBoundsException | InterruptedException se) {
            
        }
    }
    
    public static StringBuffer criaStringAleat() {
        int tamanho;
        StringBuffer randy = new StringBuffer();
        Random rand = new Random();
        
        tamanho = rand.nextInt((80)+1)+80;
        System.out.println("tamanho -> "+tamanho);
        for(int i = 0; i < tamanho; i++) {
            randy.append((char)(rand.nextInt((122 - 97) + 1) + 97));
        }
        System.out.println("aleat -> "+randy);
        
        return randy;
    }
    
    public static void main(String[] args) throws Exception{
        strFinal.set(criaStringAleat());
        fila.add(strFinal.get());
        Thread t = null;
        
        try {
            for(int i = 0; i < 30; i++) {
                t = new Thread(new StringThread(), "Thread "+i);
                t.setDaemon(true);
                t.start();
            }
            t.join();
        } catch (Exception e) {
            System.out.println("Erro na criacao/execucao de threads!");
        }
    }
}
