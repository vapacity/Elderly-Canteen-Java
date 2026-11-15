CREATE TABLE IF NOT EXISTS Account (
  accountId INT AUTO_INCREMENT,
  accountName VARCHAR(512),
  password VARCHAR(512),
  phoneNum VARCHAR(512) UNIQUE,
  verifyCode INT,
  identity VARCHAR(512),
  birthDate DATE,
  address VARCHAR(512),
  name VARCHAR(512),
  idCard VARCHAR(512),
  portrait VARCHAR(512),
  money DECIMAL(12,2),
  gender VARCHAR(512),
  PRIMARY KEY (accountId)
);

CREATE TABLE IF NOT EXISTS Cart (
  cartId INT AUTO_INCREMENT,
  accountId INT,
  createdTime DATE,
  updatedTime DATE,
  PRIMARY KEY (cartId)
);

CREATE TABLE IF NOT EXISTS CartItem (
  cartId INT,
  dishId INT,
  week DATE,
  quantity INT,
  PRIMARY KEY (cartId,dishId,week)
);

CREATE TABLE IF NOT EXISTS Category (
  cateId INT AUTO_INCREMENT,
  cateName VARCHAR(512),
  PRIMARY KEY (cateId)
);

CREATE TABLE IF NOT EXISTS Dish (
  dishId INT AUTO_INCREMENT,
  dishName VARCHAR(512),
  price DECIMAL(12,2),
  cateId INT,
  imageUrl VARCHAR(512),
  PRIMARY KEY (dishId)
);

CREATE TABLE IF NOT EXISTS WeekMenu (
  dishId INT,
  week DATE,
  stock INT,
  sales INT,
  disPrice DECIMAL(12,2),
  day VARCHAR(512),
  PRIMARY KEY (dishId,week)
);

CREATE TABLE IF NOT EXISTS Finance (
  financeId INT AUTO_INCREMENT,
  financeType VARCHAR(512),
  financeDate DATE,
  price DECIMAL(12,2),
  inOrOut VARCHAR(512),
  accountId INT,
  administratorId INT,
  proof BLOB,
  status VARCHAR(512),
  PRIMARY KEY (financeId)
);

CREATE TABLE IF NOT EXISTS OrderInfo (
  orderId INT AUTO_INCREMENT,
  deliverOrDining VARCHAR(512),
  financeId INT,
  cartId INT UNIQUE,
  status VARCHAR(512),
  bonus DECIMAL(12,2),
  remark VARCHAR(512),
  PRIMARY KEY (orderId)
);

CREATE TABLE IF NOT EXISTS DeliverOrder (
  orderId INT,
  deliverPhone VARCHAR(512),
  customerPhone VARCHAR(512),
  cusAddress VARCHAR(512),
  deliverStatus VARCHAR(512),
  cartId INT,
  PRIMARY KEY (orderId)
);

CREATE TABLE IF NOT EXISTS Senior (
  accountId INT,
  familyNum VARCHAR(512),
  subsidy DECIMAL(12,2),
  PRIMARY KEY (accountId)
);

CREATE TABLE IF NOT EXISTS DeliverV (
  orderId INT,
  volunteerId INT,
  PRIMARY KEY (orderId)
);

CREATE TABLE IF NOT EXISTS OrderReview (
  orderId INT,
  cStars DECIMAL(12,2),
  cReviewText VARCHAR(512),
  PRIMARY KEY (orderId)
);

CREATE TABLE IF NOT EXISTS DeliverReview (
  orderId INT,
  dStars DECIMAL(12,2),
  dReviewText VARCHAR(512),
  PRIMARY KEY (orderId)
);

CREATE TABLE IF NOT EXISTS SystemLogs (
  logId INT AUTO_INCREMENT,
  logLevel VARCHAR(512),
  message VARCHAR(512),
  createdAt DATE,
  PRIMARY KEY (logId)
);
