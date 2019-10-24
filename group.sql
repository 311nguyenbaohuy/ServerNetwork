USE CHATDB GO

CREATE TABLE GROUP_CHAT(
	group_name		VARCHAR(100),
	user_name		VARCHAR(100)
	PRIMARY KEY(group_name,user_name)
)
GO

INSERT INTO dbo.GROUP_CHAT VALUES ('abc', 'huy')


ALTER TABLE dbo.GROUP_CHAT ADD 
	CONSTRAINT fk_group_chat_userAccount FOREIGN KEY (user_name) REFERENCES dbo.USER_ACCOUNT(User_Name)
GO


