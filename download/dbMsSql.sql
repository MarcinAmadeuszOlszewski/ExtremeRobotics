/****** Object:  Table [dbo].[er_currency]    Script Date: 2017-07-16 22:11:08 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[er_currency](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[date] [datetime] NOT NULL,
	[usd] [money] NULL,
	[eur] [money] NULL,
	[chf] [money] NULL,
	[uah] [money] NULL,
	[czk] [money] NULL,
	[hrk] [money] NULL,
	[php] [money] NULL,
	[zar] [money] NULL,
	[rub] [money] NULL,
	[cny] [money] NULL,
	[tab_no] varchar(20) NOT NULL,
UNIQUE NONCLUSTERED 
(
	[date] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE CLUSTERED INDEX dateCluster ON [dbo].[er_currency]([date]);
GO