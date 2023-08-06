DROP TABLE if EXISTS USER;
DROP TABLE if EXISTS USER_ADDRESS;
DROP TABLE if EXISTS CATEGORY;
DROP TABLE if EXISTS PRODUCT;
DROP TABLE if EXISTS BID;
DROP TABLE if EXISTS PAYMENT;
DROP TABLE if EXISTS ORDER;
DROP TABLE if EXISTS ORDER_HISTORY;

CREATE TABLE USER
(
  ID           BIGINT AUTO_INCREMENT COMMENT '회원 번호',
  USER_ID      VARCHAR(50) NOT NULL COMMENT '회원 아이디',
  PASSWORD     VARCHAR(50) NOT NULL COMMENT '회원 비밀번호',
  USERNAME     VARCHAR(50) NOT NULL COMMENT '회원명',
  ROLE_TYPE    CHAR(1)     NOT NULL COMMENT '역할',
  PHONE_NUMBER VARCHAR(50) NOT NULL COMMENT '휴대폰 번호',
  IS_DELETED   CHAR(1)     NOT NULL DEFAULT 'N' COMMENT '삭제여부',
  CREATED_AT   DATETIME    NOT NULL COMMENT '등록일시',
  CREATED_BY   VARCHAR(50) NOT NULL COMMENT '등록자',
  UPDATED_AT   DATETIME    NULL COMMENT '수정일시',
  UPDATED_BY   VARCHAR(50) NULL COMMENT '수정자',
  PRIMARY KEY (ID)
);

CREATE TABLE USER_ADDRESS
(
  ID             BIGINT AUTO_INCREMENT COMMENT '회원 주소 번호',
  ADDRESS        VARCHAR(50) NOT NULL COMMENT '주소명',
  ADDRESS_DETAIL VARCHAR(50) NULL COMMENT '상세주소',
  PHONE_NUMBER   VARCHAR(50) NOT NULL COMMENT '휴대폰 번호',
  CREATED_AT     DATETIME    NOT NULL COMMENT '등록일시',
  CREATED_BY     VARCHAR(50) NOT NULL COMMENT '등록자',
  UPDATED_AT     DATETIME    NULL COMMENT '수정일시',
  UPDATED_BY     VARCHAR(50) NULL COMMENT '수정자',
  USER_ID        BIGINT      NOT NULL COMMENT '회원 번호',
  PRIMARY KEY (ID)
);

CREATE TABLE CATEGORY
(
  ID         BIGINT AUTO_INCREMENT COMMENT '카테고리 번호',
  NAME       VARCHAR(255) NOT NULL COMMENT '카테고리명',
  CREATED_AT DATETIME     NOT NULL COMMENT '등록일시',
  CREATED_BY VARCHAR(50)  NOT NULL COMMENT '등록자',
  UPDATED_AT DATETIME     NULL COMMENT '수정일시',
  UPDATED_BY VARCHAR(50)  NULL COMMENT '수정자',
  PRIMARY KEY (ID)
);

CREATE TABLE PRODUCT
(
  ID                BIGINT AUTO_INCREMENT COMMENT '상품 번호',
  NAME              VARCHAR(50)  NOT NULL COMMENT '상품명',
  DESCRIPTION       VARCHAR(255) NULL COMMENT '상세설명',
  QUICK_PRICE       VARCHAR(255) NOT NULL COMMENT '즉시 구입가',
  START_BID_PRICE   VARCHAR(255) NOT NULL COMMENT '시작 입찰가',
  MIN_BID_PRICE     VARCHAR(255) NOT NULL COMMENT '최소 입찰가',
  CURRENT_BID_PRICE VARCHAR(255) NOT NULL COMMENT '현재 입찰가',
  IS_DELETED        CHAR(1)      NOT NULL DEFAULT 'N' COMMENT '삭제여부',
  CREATED_AT        DATETIME     NOT NULL COMMENT '등록일시',
  CREATED_BY        VARCHAR(50)  NOT NULL COMMENT '등록자',
  UPDATED_AT        DATETIME     NULL COMMENT '수정일시',
  UPDATED_BY        VARCHAR(50)  NULL COMMENT '수정자',
  STARTED_AT        DATETIME     NULL COMMENT '경매 시작 일시',
  ENDED_AT          DATETIME     NULL COMMENT '경매 종료 일시',
  USER_ID           BIGINT       NOT NULL COMMENT '회원 번호',
  CATEGORY_ID       BIGINT       NOT NULL COMMENT '카테고리 번호',
  PRIMARY KEY (ID)
);

CREATE TABLE BID
(
  ID         BIGINT AUTO_INCREMENT COMMENT '입찰 번호',
  PRICE      VARCHAR(255) NOT NULL COMMENT '입찰 금액',
  CREATED_AT DATETIME     NOT NULL COMMENT '등록일시',
  CREATED_BY VARCHAR(50)  NOT NULL COMMENT '등록자',
  USER_ID    BIGINT       NOT NULL COMMENT '회원 번호',
  PRODUCT_ID BIGINT       NOT NULL COMMENT '상품 번호',
  PRIMARY KEY (ID)
);

CREATE TABLE PAYMENT
(
  ID          BIGINT AUTO_INCREMENT COMMENT '결제 번호',
  NAME        VARCHAR(255) NOT NULL COMMENT '결제명',
  METHOD_TYPE VARCHAR(10)  NOT NULL COMMENT '결제 방식',
  PRICE       VARCHAR(255) NOT NULL COMMENT '결제 금액',
  CREATED_AT  DATETIME     NOT NULL COMMENT '등록일시',
  CREATED_BY  VARCHAR(50)  NOT NULL COMMENT '등록자',
  ORDER_ID    BIGINT       NOT NULL COMMENT '주문 번호',
  PRIMARY KEY (ID)
);

CREATE TABLE ORDER
(
  ID          BIGINT AUTO_INCREMENT COMMENT '주문 번호',
  PRICE       VARCHAR(255) NOT NULL COMMENT '주문 금액',
  STATUS_TYPE VARCHAR(10)  NOT NULL COMMENT '주문 상태',
  CREATED_AT  DATETIME     NOT NULL COMMENT '등록일시',
  CREATED_BY  VARCHAR(50)  NOT NULL COMMENT '등록자',
  UPDATED_AT  DATETIME     NULL COMMENT '수정일시',
  UPDATED_BY  VARCHAR(50)  NULL COMMENT '수정자',
  USER_ID     BIGINT       NOT NULL COMMENT '회원 번호',
  PRODUCT_ID  BIGINT       NOT NULL COMMENT '상품 번호',
  PRIMARY KEY (ID)
);

CREATE TABLE ORDER_HISTORY
(
  ID          BIGINT AUTO_INCREMENT COMMENT '주문 이력 번호',
  PRICE       VARCHAR(255) NOT NULL COMMENT '주문 금액',
  STATUS_TYPE VARCHAR(10)  NOT NULL COMMENT '주문 상태',
  CREATED_AT  DATETIME     NOT NULL COMMENT '등록일시',
  CREATED_BY  VARCHAR(50)  NOT NULL COMMENT '등록자',
  ORDER_ID    BIGINT       NOT NULL COMMENT '주문 번호',
  PRIMARY KEY (ID)
);