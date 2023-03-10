-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 10, 2023 at 11:42 AM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 7.4.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `crm_new`
--

-- --------------------------------------------------------

--
-- Table structure for table `activity`
--

CREATE TABLE `activity` (
  `activity_id` int(11) NOT NULL,
  `user_id` varchar(25) NOT NULL,
  `activity_name` text NOT NULL,
  `video` text NOT NULL,
  `company_name` varchar(40) NOT NULL,
  `assign_to` varchar(255) NOT NULL,
  `activity_email` varchar(30) NOT NULL,
  `activity_contact` bigint(15) NOT NULL,
  `activity_addr` varchar(40) NOT NULL,
  `state_name` text NOT NULL,
  `city_name` text NOT NULL,
  `pincode` int(6) NOT NULL,
  `reminder_date` text NOT NULL,
  `images` varchar(500) NOT NULL,
  `status` varchar(30) NOT NULL DEFAULT 'in progress',
  `videos` varchar(500) NOT NULL,
  `remark` text NOT NULL,
  `active` enum('ACTIVE','INACTIVE') NOT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_on` datetime NOT NULL DEFAULT current_timestamp(),
  `modified_by` varchar(30) NOT NULL,
  `modified_on` datetime NOT NULL DEFAULT current_timestamp(),
  `disabled_by` varchar(30) NOT NULL,
  `disabled_on` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `activity_log`
--

CREATE TABLE `activity_log` (
  `id` int(11) NOT NULL,
  `activity_id` varchar(255) NOT NULL,
  `user_id` varchar(25) NOT NULL,
  `activity_name` text NOT NULL,
  `company_name` varchar(40) NOT NULL,
  `assign_to` varchar(255) NOT NULL,
  `activity_email` varchar(30) NOT NULL,
  `activity_contact` bigint(15) NOT NULL,
  `activity_addr` varchar(40) NOT NULL,
  `state_name` text NOT NULL,
  `city_name` text NOT NULL,
  `pincode` int(6) NOT NULL,
  `reminder_date` text NOT NULL,
  `status` varchar(20) NOT NULL,
  `images` varchar(500) NOT NULL,
  `videos` varchar(500) NOT NULL,
  `remark` text NOT NULL,
  `active` enum('ACTIVE','INACTIVE') NOT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_on` datetime NOT NULL DEFAULT current_timestamp(),
  `modified_by` varchar(30) NOT NULL,
  `modified_on` datetime NOT NULL DEFAULT current_timestamp(),
  `disabled_by` varchar(30) NOT NULL,
  `disabled_on` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `categories_id` int(11) NOT NULL,
  `categories_name` varchar(30) NOT NULL,
  `categories_sort_order` int(40) NOT NULL,
  `image` varchar(400) NOT NULL,
  `categories_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`categories_id`, `categories_name`, `categories_sort_order`, `image`, `categories_date`) VALUES
(1, 'sanket', 124, 'bird.png', '2022-11-26'),
(2, 'pallavi', 456, 'bird.png', '2022-11-27'),
(3, 'swapnil', 8888, 'profile-img.jpg', '2022-11-25'),
(4, 'mehek', 7777, 'messages-2.jpg', '2022-11-11'),
(5, 'mehek', 4444, 'messages-2.jpg', '2022-11-04');

-- --------------------------------------------------------

--
-- Table structure for table `city`
--

CREATE TABLE `city` (
  `city_id` int(11) NOT NULL,
  `state_name` varchar(40) NOT NULL,
  `city_name` varchar(40) NOT NULL,
  `city_sort_order` int(6) NOT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_on` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `city`
--

INSERT INTO `city` (`city_id`, `state_name`, `city_name`, `city_sort_order`, `created_by`, `created_on`) VALUES
(6, 'Maharastra', 'pune', 8, '', '2022-11-18 15:17:44'),
(7, 'Gujarat', 'borivali', 789456, '', '2022-11-18 15:17:44'),
(8, 'Arunachal Pradesh', 'satara', 8, '', '2022-11-21 15:20:56'),
(9, 'Andhra Pradesh', 'pune', 8, '', '2022-11-21 15:21:37'),
(10, 'Assam', ' Guwahati.', 9, '', '2022-11-21 16:39:17'),
(11, 'Assam', ' Silchar ', 5, '', '2022-11-21 16:39:40'),
(12, 'Assam', 'Tezpur', 9, '', '2022-11-21 16:39:58'),
(13, 'Andhra Pradesh', 'Guntur', 1, '', '2022-11-21 16:40:34'),
(14, 'Andhra Pradesh', ' Nellore ', 2, '', '2022-11-21 16:40:52'),
(15, 'Andhra Pradesh', 'Kurnool ', 3, '', '2022-11-21 16:41:04');

-- --------------------------------------------------------

--
-- Table structure for table `pictures`
--

CREATE TABLE `pictures` (
  `id` int(11) NOT NULL,
  `userid` int(15) NOT NULL,
  `actid` int(15) NOT NULL,
  `picture` text NOT NULL,
  `assign_to` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `pincode`
--

CREATE TABLE `pincode` (
  `id` int(11) NOT NULL,
  `state_name` varchar(40) NOT NULL,
  `city_name` varchar(40) NOT NULL,
  `pincode` int(6) NOT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_on` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pincode`
--

INSERT INTO `pincode` (`id`, `state_name`, `city_name`, `pincode`, `created_by`, `created_on`) VALUES
(1, 'Maharastra', 'nashik', 423021, '', '2022-11-18 15:18:52'),
(3, 'Maharastra', 'pune', 123123, '', '2022-11-18 15:18:52'),
(4, 'Maharastra', 'pune', 123123, '', '2022-11-18 15:18:52'),
(5, 'Gujarat', 'Mumbai', 123123, '', '2022-11-18 15:18:52'),
(6, 'Gujarat', 'borivali', 896312, '', '2022-11-18 15:18:52'),
(7, 'Maharastra', 'pune', 888888, '', '2022-11-18 15:18:52'),
(8, 'Maharastra', 'pune', 111111, '', '2022-11-18 15:23:12');

-- --------------------------------------------------------

--
-- Table structure for table `state`
--

CREATE TABLE `state` (
  `state_id` int(11) NOT NULL,
  `state_name` varchar(40) NOT NULL,
  `state_sort_order` int(30) NOT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_on` datetime NOT NULL DEFAULT current_timestamp(),
  `modified_by` varchar(30) NOT NULL,
  `modified_on` datetime NOT NULL DEFAULT current_timestamp(),
  `disabled_by` varchar(30) NOT NULL,
  `disabled_on` datetime NOT NULL DEFAULT current_timestamp(),
  `status` enum('ACTIVE','INACTIVE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `state`
--

INSERT INTO `state` (`state_id`, `state_name`, `state_sort_order`, `created_by`, `created_on`, `modified_by`, `modified_on`, `disabled_by`, `disabled_on`, `status`) VALUES
(1, 'Maharastra', 1, '', '2022-11-18 15:16:51', '', '2022-11-21 07:35:37', '', '2022-11-18 15:37:21', 'ACTIVE'),
(2, 'Gujarat', 2, '', '2022-11-18 15:16:51', '', '2022-11-18 15:37:21', '', '2022-11-18 15:37:21', 'ACTIVE'),
(4, 'Assam', 3, '', '2022-11-18 15:16:51', '', '2022-11-18 13:46:28', '', '2022-11-18 15:37:21', 'ACTIVE'),
(5, 'Andhra Pradesh', 4, '', '2022-11-21 12:15:52', '', '2022-11-21 12:15:52', '', '2022-11-21 12:15:52', 'ACTIVE'),
(6, 'Arunachal Pradesh', 5, '', '2022-11-21 12:16:22', '', '2022-11-21 07:46:33', '', '2022-11-21 12:16:22', 'ACTIVE'),
(7, 'Bihar', 6, '', '2022-11-21 12:17:08', '', '2022-11-21 12:17:08', '', '2022-11-21 12:17:08', 'ACTIVE');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(40) NOT NULL,
  `fname` text NOT NULL,
  `lname` text NOT NULL,
  `added_by` varchar(100) DEFAULT NULL,
  `password` varchar(15) NOT NULL,
  `email` varchar(30) NOT NULL,
  `phone` bigint(15) NOT NULL,
  `company_name` varchar(40) NOT NULL,
  `addr` varchar(60) NOT NULL,
  `state_name` varchar(30) NOT NULL,
  `city_name` varchar(30) NOT NULL,
  `pincode` int(6) NOT NULL,
  `active` enum('ACTIVE','INACTIVE') NOT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_on` datetime NOT NULL DEFAULT current_timestamp(),
  `modified_by` varchar(30) NOT NULL,
  `disabled_by` varchar(30) NOT NULL,
  `disabled_on` varchar(30) NOT NULL,
  `role` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `fname`, `lname`, `added_by`, `password`, `email`, `phone`, `company_name`, `addr`, `state_name`, `city_name`, `pincode`, `active`, `created_by`, `created_on`, `modified_by`, `disabled_by`, `disabled_on`, `role`) VALUES
(1, '', 'Nishant', 'Yevlekar', NULL, 'nishant', 'nishant@gmail.com', 8888888888, '', 'Room no 83', 'Meghalaya', 'Vashi', 658908, 'ACTIVE', '', '2023-03-04 13:57:33', '', '', '', 'Admin'),
(3, '', 'Jay', 'Mhatre', 'Nishant Yevlekar', 'jay', 'jay@gmail.com', 9090909090, '', 'woow', 'siis', 'siix', 468606, 'ACTIVE', '', '2023-03-04 14:04:51', '', '', '', 'Executive');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activity`
--
ALTER TABLE `activity`
  ADD PRIMARY KEY (`activity_id`);

--
-- Indexes for table `activity_log`
--
ALTER TABLE `activity_log`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`categories_id`);

--
-- Indexes for table `city`
--
ALTER TABLE `city`
  ADD PRIMARY KEY (`city_id`);

--
-- Indexes for table `pictures`
--
ALTER TABLE `pictures`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pincode`
--
ALTER TABLE `pincode`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `state`
--
ALTER TABLE `state`
  ADD PRIMARY KEY (`state_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activity`
--
ALTER TABLE `activity`
  MODIFY `activity_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `activity_log`
--
ALTER TABLE `activity_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `categories_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `city`
--
ALTER TABLE `city`
  MODIFY `city_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `pictures`
--
ALTER TABLE `pictures`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pincode`
--
ALTER TABLE `pincode`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `state`
--
ALTER TABLE `state`
  MODIFY `state_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
