CREATE TABLE TB_DEVICE(
    ID_DEVICE BIGSERIAL PRIMARY KEY,
    NM_MANUFACTURER VARCHAR(100) NOT NULL,
    TX_NOTIFICATION_TOKEN VARCHAR(255) NOT NULL,
    NM_OS VARCHAR(100) NOT NULL,
    TX_OS_VERSION VARCHAR(20) NOT NULL,
    TX_DEVICE_TYPE VARCHAR(20) NOT NULL,
    ID_USER INT NOT NULL,
    CONSTRAINT FK_USER FOREIGN KEY(ID_USER) REFERENCES TB_USER(ID_USER)
);