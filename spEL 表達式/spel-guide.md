# SpEL 簡易教學

## SpEL 簡介

**Spring Expression Language (SpEL)** 是 Spring 框架提供的 Java 表達式語言，  
常用於 **動態運算** 與 **邏輯判斷**。  

---

## 語法格式

### 1. 變數

變數需要使用 `Map<String, Object>` 型態進行保存：  

- key：變數名稱  
- value：數值  

**範例：**

```java
Map<String, Object> result = new HashMap<>();
result.put("age", 30);
result.put("income", 50000);
result.put("planClasCode", "9A21");
```

---

### 2. 判斷語法

執行 SpEL 判斷時，基本的語法結構如下：

```java
ExpressionParser parser = new SpelExpressionParser();
EvaluationContext context = new StandardEvaluationContext();
變數Map.forEach(context::setVariable);

String 判斷式 = "spEL 表達式 ? 符合結果 : 不符合結果";
結果型態 result = parser.parseExpression(判斷式)
        .getValue(context, 結果型態.class);
```

說明：

- `#`：在 SpEL 表達式中代表變數，如 `#age`。
- 判斷式使用 **三元運算子** (`條件 ? 符合結果 : 不符合結果`)。
- `getValue()` 第二個參數為結果型態的 `class`，若未指定則回傳 `Object`。

**範例：**

```java
ExpressionParser parser = new SpelExpressionParser();
EvaluationContext context = new StandardEvaluationContext();
dataMap.forEach(context::setVariable);

String rule = "#age >= 18 ? true : false";
Boolean result = parser.parseExpression(rule)
        .getValue(context, Boolean.class);
```

---

## SpEL 表達式

### 1. 比較運算子

| 運算子  | 說明   | 範例                        |
| ---- | ---- | ------------------------- |
| `==` | 等於   | `#age == 18`              |
| `!=` | 不等於  | `#planClasCode != '9A21'` |
| `>`  | 大於   | `#income > 30000`         |
| `<`  | 小於   | `#age < 65`               |
| `>=` | 大於等於 | `#age >= 18`              |
| `<=` | 小於等於 | `#income <= 100000`       |

---

### 2. 算術運算子

| 運算子 | 說明  | 範例                         |
| --- | --- | -------------------------- |
| `+` | 加法  | `#x + #y == 10`            |
| `-` | 減法  | `#x - #y > 0`              |
| `*` | 乘法  | `#price * #quantity > 100` |
| `/` | 除法  | `#income / 12`             |
| `%` | 餘數  | `#age % 2 == 0`            |
| `^` | 次方  | `#x ^ 2 > 25`              |

---

### 3. 邏輯運算子

| 運算子   | 說明               | 範例                                    |
| ----- | ---------------- | ------------------------------------- |
| `and` | 且                | `#age >= 18 and #status == 'OK'`      |
| `or`  | 或                | `#status == 'OK' or #count > 0`       |
| `not` | 反轉（true ⇄ false） | `not (#status == 'OK' or #count > 0)` |

---

### 4. 正則比對

| 運算子       | 說明      | 範例                                                   |
| --------- | ------- | ---------------------------------------------------- |
| `matches` | 正則運算式比對 | `#email matches '^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$'` |

---

### 5. 集合操作

| 用法            | 說明        | 範例                    |
| ------------- | --------- | --------------------- |
| `集合變數[index]` | 取得指定索引的元素 | `#list[0]`            |
| `size()`      | 取得集合長度    | `#list.size()`        |
| `contains()`  | 判斷是否包含元素  | `#list.contains('A')` |

---

### 6. 函式

SpEL 可直接調用 JAVA 的基本方法，如：**String 的基本方法**。

| 函式                      | 說明        | 範例                              |
| ----------------------- | --------- | ------------------------------- |
| `toUpperCase()`         | 轉為大寫      | `#name.toUpperCase()`           |
| `toLowerCase()`         | 轉為小寫      | `#name.toLowerCase()`           |
| `substring(start, end)` | 擷取字串      | `#name.substring(0,2)`          |
| `isEmpty()`             | 是否為空字串    | `#name.isEmpty()`               |
| `contains(text)`        | 是否包含指定文字  | `#planClasCode.contains('9A')`  |
| `startsWith(text)`      | 是否以指定字首開頭 | `#planClasCode.startsWith('9')` |
| `endsWith(text)`        | 是否以指定字尾結尾 | `#planClasCode.endsWith('1')`   |

---

### 7. 物件資料

SpEL 不僅可操作基本型別，也能直接使用 **物件 (DTO)** 中的屬性。

當變數為物件時，可透過 `#變數.屬性` 方式存取屬性值，

甚至支援 巢狀屬性存取（如 `#user.department.name`）。

- 範例
  
  - DTO 
    
    ```java
    public class UserDto {
        private String userCode;
        private String userName;
        private String userDept;
    
        // Getter / Setter 省略
    }
    ```
  
  - 取值範例
    
    ```java
    Map<String, Object> dataMap = new HashMap<>();
    dataMap.put("user", new UserDto("ABC001", "測試人員", "90250"));
    
    ExpressionParser parser = new SpelExpressionParser();
    EvaluationContext context = new StandardEvaluationContext();
    dataMap.forEach(context::setVariable);
    
    String userCode = parser.parseExpression("#user.userCode")
            .getValue(context, String.class);
    
    System.out.println(userCode); // 輸出：ABC001
    ```
  
  - 判斷範例
    
    ```java
    Map<String, Object> dataMap = new HashMap<>();
    dataMap.put("user", new UserDto("ABC001", "測試人員", "90250"));
    
    ExpressionParser parser = new SpelExpressionParser();
    EvaluationContext context = new StandardEvaluationContext();
    dataMap.forEach(context::setVariable);
    
    String rule = "#user.userDept == '90250' ? '同部門' : '其他部門'";
    String result = parser.parseExpression(rule)
            .getValue(context, String.class);
    
    System.out.println(result); // 輸出：同部門
    ```

---

## **完整範例**

```java
Map<String, Object> data = new HashMap<>();
data.put("age", 22);
data.put("name", "John");
data.put("email", "test@example.com");

ExpressionParser parser = new SpelExpressionParser();
EvaluationContext context = new StandardEvaluationContext();
data.forEach(context::setVariable);

String rule = "#age >= 18 and #email.matches('^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$') ? '合格' : '不合格'";
String result = parser.parseExpression(rule).getValue(context, String.class);

System.out.println(result); // 輸出：合格
```
