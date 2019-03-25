package com.sbolo.syk.common.tools;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.sbolo.syk.common.enums.MatchRuleEnum;

public class SensitiveWordUtils {
	private static Map<Object,Object> sensitiveWordsMap = new HashMap<>();

    private static final String END_FLAG="end";

    public static void add(Set<String> sensitiveWords) throws Exception{
    	if(sensitiveWords==null||sensitiveWords.isEmpty()){
            throw new IllegalArgumentException("Senditive words must not be empty!");
        }
//		sensitiveWordsMap = new HashMap<>(sensitiveWords.size());
        String currentWord;
        Map<Object,Object> currentMap;
        Map<Object,Object> subMap;
        Iterator<String> iterator = sensitiveWords.iterator();
        while (iterator.hasNext()){
            currentWord = iterator.next();
            if(currentWord == null||currentWord.trim().length()<2){  //敏感词长度必须大于等于2
                continue;
            }
            currentMap = sensitiveWordsMap;
            for(int i=0;i<currentWord.length();i++){
                char c = currentWord.charAt(i);
                subMap = (Map<Object, Object>) currentMap.get(c);
                if(subMap == null){
                    subMap = new HashMap<>();
                    currentMap.put(c,subMap);
                    currentMap = subMap;
                }else {
                    currentMap = subMap;
                }
                if(i == currentWord.length()-1){
                    //如果是最后一个字符，则put一个结束标志，这里只需要保存key就行了，value为null可以节省空间。
                    //如果不是最后一个字符，则不需要存这个结束标志，同样也是为了节省空间。
                    currentMap.put(END_FLAG,null);
                }
            }
        }
    }
    
    public static Set<String> getSensitiveWords(String text, MatchRuleEnum matchRule){
        if(text==null||text.trim().length()==0){
            throw new IllegalArgumentException("The input text must not be empty.");
        }
        Set<String> sensitiveWords=new HashSet<>();
        for(int i=0;i<text.length();i++){
            int sensitiveWordLength = getSensitiveWordLength(text, i, matchRule);
            if(sensitiveWordLength>0){
                String sensitiveWord = text.substring(i, i + sensitiveWordLength);
                sensitiveWords.add(sensitiveWord);
                if(matchRule == MatchRuleEnum.MIN_MATCH){
                    break;
                }
                i=i+sensitiveWordLength-1;
            }
        }
        return sensitiveWords;
    }

    private static int getSensitiveWordLength(String text,int startIndex,MatchRuleEnum matchRule){
        if(text==null||text.trim().length()==0){
            throw new IllegalArgumentException("The input text must not be empty.");
        }
        char currentChar;
        Map<Object,Object> currentMap=sensitiveWordsMap;
        int wordLength=0;
        boolean endFlag=false;
        for(int i=startIndex;i<text.length();i++){
            currentChar=text.charAt(i);
            Map<Object,Object> subMap=(Map<Object,Object>) currentMap.get(currentChar);
            if(subMap==null){
                break;
            }else {
                wordLength++;
                if(subMap.containsKey(END_FLAG)){
                    endFlag=true;
                    if(matchRule == MatchRuleEnum.MIN_MATCH){
                        break;
                    }else {
                        currentMap=subMap;
                    }
                }else {
                    currentMap=subMap;
                }
            }
        }
        if(!endFlag){
            wordLength=0;
        }
        return wordLength;
    }
}
