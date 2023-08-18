# SmoothUsers

Simple plugin to work easier with users in cross-server environments.
If you are searching how to use the API -> [SmoothUsersAPI](https://github.com/Smooth-Plugins/SmoothUsersAPI)

Example of a use case:
Consider a cross-server survival scenario with two servers: Server A and Server B.
In this situation, Player A is located on Server A, while Player B is on Server B.
Suppose Player B wishes to invite Player A to their clan using the command "/clan invite username"
However, there's a challenge because Player A has never previously joined Server B.
As a result, utilizing the "Bukkit.getPlayer(username)" function is not feasible.

In this circumstance, acquiring the UUID of Player A becomes necessary.
Unfortunately, you only possess Player A's nickname, and it's possible that the letter casing is incorrect.
This is where the SmoothUsers plugin proves invaluable – it enables you to overcome this issue and obtain the accurate UUID of Player A.

## 📊 Databases

- MongoDB (required)
- Redis (required)

## 🧠 Dependencies

- None

## 🔭 Compatibility

- Java 16 or higher
- Paper 1.16.5 or higher (or any Paper fork)

## 🧾 Commands and permissions

- None

## ✏️ PlaceholderAPI placeholders

- None

## 📋 Notes

- None

## 👪 Authors

[Alex](https://github.com/alexcastro1919)


