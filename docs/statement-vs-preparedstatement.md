# Statement vs PreparedStatement in Java

## Overview

In JDBC, `Statement` and `PreparedStatement` are used to execute SQL queries, but they differ significantly in usage, performance, and security.

---

## Key Differences

| Feature                  | Statement                                | PreparedStatement                               |
|--------------------------|-------------------------------------------|--------------------------------------------------|
| SQL Injection Protection | ❌ Vulnerable                             | ✅ Safe (uses parameterized queries)            |
| Query Compilation        | Compiled every time                       | Precompiled and stored                          |
| Performance              | Slower for repeated queries               | Faster for repeated execution                   |
| Code Clarity             | Harder to read with concatenated values   | Clear with placeholders (?)                     |

---

## SQL Injection Example

### Vulnerable Code (Using Statement)

```java
String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(query);
```

**Injection Input:**

- Username: `admin' --`
- Password: `anything`

**Result:**

```sql
SELECT * FROM users WHERE username = 'admin' --' AND password = 'anything'
```

The condition after `--` is ignored, allowing unauthorized access.

---

### Safe Code (Using PreparedStatement)

```java
String query = "SELECT * FROM users WHERE username = ? AND password = ?";
PreparedStatement pstmt = conn.prepareStatement(query);
pstmt.setString(1, username);
pstmt.setString(2, password);
ResultSet rs = pstmt.executeQuery();
```

**Injection Attempt:** Fails, since user input is parameterized and not executed as SQL code.

---

## Conclusion

Use `PreparedStatement` instead of `Statement` to:

- Prevent SQL injection
- Improve performance for repeated queries
- Write cleaner and safer code

