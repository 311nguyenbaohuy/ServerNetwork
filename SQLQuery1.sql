CREATE DATABASE CHATDB
GO

USE CHATDB
GO

CREATE TABLE USER_ACCOUNT(
	User_Id			INT NOT NULL IDENTITY PRIMARY KEY,
	User_Name		VARCHAR(100) UNIQUE,	-- Ten dang nhap
	User_Password	VARCHAR(100),			-- Mat khau
	IP_addr			VARCHAR(100),
	Status			BIT DEFAULT 0			-- Trang thai ON/OFF
)
GO

CREATE TABLE FRIEND(
	User_Id			INT,
	Friend_Id		INT
	PRIMARY KEY(User_Id, Friend_Id)
)

CREATE TABLE FRIEND_REQUEST(
	User_Id			INT,
	Friend_Id		INT
	PRIMARY KEY(User_Id, Friend_Id)
)

GO


INSERT INTO dbo.USER_ACCOUNT (User_Name, User_Password) VALUES ('huy', '1')
INSERT INTO dbo.USER_ACCOUNT (User_Name, User_Password) VALUES ('trung', '1')
INSERT INTO dbo.USER_ACCOUNT (User_Name, User_Password) VALUES ('bao', '1')
INSERT INTO dbo.USER_ACCOUNT (User_Name, User_Password) VALUES ('huyyyy', '1')
GO


INSERT INTO dbo.FRIEND_REQUEST (User_Id, Friend_Id) VALUES (2,3)
INSERT INTO dbo.FRIEND (User_Id, Friend_Id) VALUES (1,2)
INSERT INTO dbo.FRIEND (User_Id, Friend_Id) VALUES (1,3)
INSERT INTO dbo.FRIEND (User_Id, Friend_Id) VALUES (2,1)
INSERT INTO dbo.FRIEND (User_Id, Friend_Id) VALUES (3,1)
GO



ALTER TABLE dbo.FRIEND_REQUEST ADD 
	CONSTRAINT fk_friend_request_user FOREIGN KEY (User_Id) REFERENCES dbo.USER_ACCOUNT(User_Id),
	CONSTRAINT fk_friend_request_id FOREIGN KEY (Friend_Id) REFERENCES dbo.USER_ACCOUNT(User_Id)
GO

USE [CHATDB]
GO

ALTER TABLE [dbo].[FRIEND]  WITH CHECK ADD  CONSTRAINT [fk_friend_id] FOREIGN KEY([Friend_Id])
REFERENCES [dbo].[USER_ACCOUNT] ([User_Id])
GO

ALTER TABLE [dbo].[FRIEND] CHECK CONSTRAINT [fk_friend_id]
GO


USE [CHATDB]
GO

ALTER TABLE [dbo].[FRIEND]  WITH CHECK ADD  CONSTRAINT [fk_friend_user] FOREIGN KEY([User_Id])
REFERENCES [dbo].[USER_ACCOUNT] ([User_Id])
GO

ALTER TABLE [dbo].[FRIEND] CHECK CONSTRAINT [fk_friend_user]
GO


CREATE PROCEDURE p_add_list_friend (@user_id INT, @friend_id INT)
AS
BEGIN 
	IF EXISTS (SELECT * FROM dbo.FRIEND WHERE USER_ID = @user_id AND Friend_Id = @friend_id)
	BEGIN
		RETURN -1
	END

	ELSE IF EXISTS (SELECT * FROM dbo.FRIEND_REQUEST WHERE USER_ID = @user_id AND Friend_Id = @friend_id) 
	BEGIN
		INSERT INTO dbo.FRIEND (User_Id, Friend_Id) VALUES (@user_id, @friend_id)
		INSERT INTO dbo.FRIEND (User_Id, Friend_Id) VALUES (@friend_id, @user_id)
		DELETE FROM dbo.FRIEND_REQUEST WHERE USER_ID = @user_id AND Friend_Id = @friend_id
		RETURN 0
	END
	ELSE
		RETURN - 1
END
GO

