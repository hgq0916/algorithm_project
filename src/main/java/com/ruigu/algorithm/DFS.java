package com.ruigu.algorithm;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author hugangquan
 * @date 2021/06/06 09:38
 */
public class DFS {

    static int m;//顶点个数
    static int n;//路径个数

    static int[][] map = new int[100][100];

    static int[] visit = new int[100];

    static int[] stack = new int[100];

    static int end=0;

    static int top=0;

    public static void dfs(int pos){

        if(pos == end){
            System.out.println("匹配到一条路径:");
            System.out.println("------------------------");
            for(int i=0;i<top;i++){
                System.out.println(stack[i]);
            }
            System.out.println(end);
            System.out.println("------------------------");
        }

        //将该节点标记为已访问
        visit[pos] = 1;

        //将该节点入栈
        stack[top++] = pos;

        for(int i=1;i<=m;i++){
            if(map[pos][i]!=0 && visit[i]!=1){
                dfs(i);
            }
        }

        //将该节点标记为未访问
        visit[pos] = 0;
        //将该节点出栈
        top--;

    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入顶点，路径个数:");
        m = scanner.nextInt();
        n = scanner.nextInt();

        System.out.println("请输入所有路径:");
        for(int i=0;i<n;i++){
            int s = scanner.nextInt();
            int d = scanner.nextInt();
            map[s][d] = 1;
        }

        System.out.println("请输入起点终点:");
        int start = scanner.nextInt();
        end = scanner.nextInt();

        dfs(start);
    }

}
