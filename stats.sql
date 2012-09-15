--
-- Table structure for table `stats_serverid`
--
CREATE TABLE IF NOT EXISTS `stats` (
  `id` int(11) NOT NULL,
  `blocks_placed` int(11) unsigned NOT NULL DEFAULT '0',
  `blocks_broken` int(11) unsigned NOT NULL DEFAULT '0',
  `players_killed` int(11) unsigned NOT NULL DEFAULT '0',
  `mobs_killed` int(10) unsigned NOT NULL DEFAULT '0',
  `times_died` int(11) unsigned NOT NULL DEFAULT '0',
  `times_chatted` int(11) unsigned NOT NULL DEFAULT '0',
  `distance_moved` double unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `stats_timeline_serverid`
--

CREATE TABLE IF NOT EXISTS `stats_timeline` (
  `id` int(11) NOT NULL,
  `blocks_placed` int(11) unsigned NOT NULL DEFAULT '0',
  `blocks_broken` int(11) unsigned NOT NULL DEFAULT '0',
  `players_killed` int(11) unsigned NOT NULL DEFAULT '0',
  `mobs_killed` int(10) unsigned NOT NULL DEFAULT '0',
  `times_died` int(11) unsigned NOT NULL DEFAULT '0',
  `times_chatted` int(11) unsigned NOT NULL DEFAULT '0',
  `distance_moved` double unsigned NOT NULL DEFAULT '0',
  `timestamp` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
