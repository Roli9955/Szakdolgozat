USE [master]   
GO
CREATE DATABASE [Szakdolgozat]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Szakdolgozat', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.MSSQLSERVER\MSSQL\DATA\Szakdolgozat.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'Szakdolgozat_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.MSSQLSERVER\MSSQL\DATA\Szakdolgozat_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [Szakdolgozat] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Szakdolgozat].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Szakdolgozat] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Szakdolgozat] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Szakdolgozat] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Szakdolgozat] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Szakdolgozat] SET ARITHABORT OFF 
GO
ALTER DATABASE [Szakdolgozat] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Szakdolgozat] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Szakdolgozat] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Szakdolgozat] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Szakdolgozat] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Szakdolgozat] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Szakdolgozat] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Szakdolgozat] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Szakdolgozat] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Szakdolgozat] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Szakdolgozat] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Szakdolgozat] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Szakdolgozat] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Szakdolgozat] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Szakdolgozat] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Szakdolgozat] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Szakdolgozat] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Szakdolgozat] SET RECOVERY FULL 
GO
ALTER DATABASE [Szakdolgozat] SET  MULTI_USER 
GO
ALTER DATABASE [Szakdolgozat] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Szakdolgozat] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Szakdolgozat] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Szakdolgozat] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Szakdolgozat] SET DELAYED_DURABILITY = DISABLED 
GO
EXEC sys.sp_db_vardecimal_storage_format N'Szakdolgozat', N'ON'
GO
ALTER DATABASE [Szakdolgozat] SET QUERY_STORE = OFF
GO
USE [Szakdolgozat]
GO
/****** Object:  Table [dbo].[Activity]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Activity](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[description] [nchar](500) NOT NULL,
	[is_task] [tinyint] NOT NULL,
	[min] [int] NULL,
	[deadline] [datetime] NULL,
	[is_completed] [tinyint] NULL,
	[locked] [tinyint] NOT NULL,
	[date] [date] NOT NULL,
	[activity_group_id] [int] NOT NULL,
 CONSTRAINT [PK_Activity_1] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Activity_group]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Activity_group](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[parent] [int] NULL,
	[name] [nchar](45) NOT NULL,
	[can_add_child] [tinyint] NOT NULL,
	[can_add_activity] [tinyint] NOT NULL,
 CONSTRAINT [PK_Activity_group] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Holiday]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Holiday](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[holiday_from] [date] NOT NULL,
	[holiday_to] [date] NOT NULL,
	[user_id] [int] NOT NULL,
 CONSTRAINT [PK_Holiday] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Permission]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Permission](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nchar](45) NOT NULL,
 CONSTRAINT [PK_Permission] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Permission_detail]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Permission_detail](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nchar](45) NOT NULL,
 CONSTRAINT [PK_Permission_detail] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Permission_details]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Permission_details](
	[permission_id] [int] NOT NULL,
	[permission_detail_id] [int] NOT NULL,
 CONSTRAINT [PK_Permission_details] PRIMARY KEY CLUSTERED 
(
	[permission_id] ASC,
	[permission_detail_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[first_name] [nchar](45) NOT NULL,
	[last_name] [nchar](45) NOT NULL,
	[email] [nchar](45) NOT NULL,
	[login_name] [nchar](45) NOT NULL,
	[password] [nchar](45) NOT NULL,
	[last_login] [datetime] NULL,
	[max_holiday] [int] NULL,
	[can_log_in] [tinyint] NOT NULL,
	[permission_id] [int] NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User_activity]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User_activity](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[activity_id] [int] NOT NULL,
	[owner_id] [int] NOT NULL,
 CONSTRAINT [PK_User_activity] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User_work_group]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User_work_group](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[work_group_id] [int] NOT NULL,
	[in_work_group_from] [date] NOT NULL,
	[in_work_group_to] [date] NULL,
 CONSTRAINT [PK_User_work_group_1] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Work_group]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Work_group](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nchar](45) NOT NULL,
	[scale] [int] NOT NULL,
 CONSTRAINT [PK_Work_group] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Work_group_activity_group]    Script Date: 2019. 09. 13. 23:29:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Work_group_activity_group](
	[work_group_id] [int] NOT NULL,
	[activity_group_id] [int] NOT NULL,
 CONSTRAINT [PK_Work_group_activity_group_1] PRIMARY KEY CLUSTERED 
(
	[work_group_id] ASC,
	[activity_group_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Activity]  WITH CHECK ADD  CONSTRAINT [FK_Activity_Activity_group1] FOREIGN KEY([activity_group_id])
REFERENCES [dbo].[Activity_group] ([id])
GO
ALTER TABLE [dbo].[Activity] CHECK CONSTRAINT [FK_Activity_Activity_group1]
GO
ALTER TABLE [dbo].[Holiday]  WITH CHECK ADD  CONSTRAINT [FK_Holiday_User] FOREIGN KEY([user_id])
REFERENCES [dbo].[User] ([id])
GO
ALTER TABLE [dbo].[Holiday] CHECK CONSTRAINT [FK_Holiday_User]
GO
ALTER TABLE [dbo].[Permission_details]  WITH CHECK ADD  CONSTRAINT [FK_Permission_permission_detail_Permission] FOREIGN KEY([permission_id])
REFERENCES [dbo].[Permission] ([id])
GO
ALTER TABLE [dbo].[Permission_details] CHECK CONSTRAINT [FK_Permission_permission_detail_Permission]
GO
ALTER TABLE [dbo].[Permission_details]  WITH CHECK ADD  CONSTRAINT [FK_Permission_permission_detail_Permission_details] FOREIGN KEY([permission_id])
REFERENCES [dbo].[Permission_detail] ([id])
GO
ALTER TABLE [dbo].[Permission_details] CHECK CONSTRAINT [FK_Permission_permission_detail_Permission_details]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [FK_User_Permission] FOREIGN KEY([permission_id])
REFERENCES [dbo].[Permission] ([id])
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [FK_User_Permission]
GO
ALTER TABLE [dbo].[User_activity]  WITH CHECK ADD  CONSTRAINT [FK_User_activity_Activity] FOREIGN KEY([activity_id])
REFERENCES [dbo].[Activity] ([id])
GO
ALTER TABLE [dbo].[User_activity] CHECK CONSTRAINT [FK_User_activity_Activity]
GO
ALTER TABLE [dbo].[User_activity]  WITH CHECK ADD  CONSTRAINT [FK_User_activity_User1] FOREIGN KEY([user_id])
REFERENCES [dbo].[User] ([id])
GO
ALTER TABLE [dbo].[User_activity] CHECK CONSTRAINT [FK_User_activity_User1]
GO
ALTER TABLE [dbo].[User_work_group]  WITH CHECK ADD  CONSTRAINT [FK_User_work_group_User] FOREIGN KEY([user_id])
REFERENCES [dbo].[User] ([id])
GO
ALTER TABLE [dbo].[User_work_group] CHECK CONSTRAINT [FK_User_work_group_User]
GO
ALTER TABLE [dbo].[User_work_group]  WITH CHECK ADD  CONSTRAINT [FK_User_work_group_Work_group] FOREIGN KEY([work_group_id])
REFERENCES [dbo].[Work_group] ([id])
GO
ALTER TABLE [dbo].[User_work_group] CHECK CONSTRAINT [FK_User_work_group_Work_group]
GO
ALTER TABLE [dbo].[Work_group_activity_group]  WITH CHECK ADD  CONSTRAINT [FK_Work_group_activity_group_Activity_group] FOREIGN KEY([work_group_id])
REFERENCES [dbo].[Activity_group] ([id])
GO
ALTER TABLE [dbo].[Work_group_activity_group] CHECK CONSTRAINT [FK_Work_group_activity_group_Activity_group]
GO
ALTER TABLE [dbo].[Work_group_activity_group]  WITH CHECK ADD  CONSTRAINT [FK_Work_group_activity_group_Work_group] FOREIGN KEY([work_group_id])
REFERENCES [dbo].[Work_group] ([id])
GO
ALTER TABLE [dbo].[Work_group_activity_group] CHECK CONSTRAINT [FK_Work_group_activity_group_Work_group]
GO
USE [master]
GO
ALTER DATABASE [Szakdolgozat] SET  READ_WRITE 
GO
