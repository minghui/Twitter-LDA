Twitter-LDA
===========
/*
 * Copyright (C) 2012 by
 * 
 *   SMU Text Mining Group
 *	Singapore Management University
 *
 * TwitterLDA is distributed for research purpose, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * The original paper is as follows:
 * Wayne Xin Zhao, Jing Jiang et al., Comparing Twitter and traditional media using topic models. 
 * ECIR'11. (http://link.springer.com/chapter/10.1007%2F978-3-642-20161-5_34)
 * 
 * Note that the package here is not developed by the authors
 * in the paper, nor used in the original papers. It's an implementation
 * based on the paper, where most of the work is done by qiming.diao.2010@smu.edu.sg.
 * 
 * Feel free to contact the following people if you find any
 * problems in the package.
 * 
 * minghui.qiu.2010@smu.edu.sg
 *
 */

Brief introduction:

Latent Dirichlet Allocation (LDA) has been widely used in textual analysis. The original LDA is used to find hidden "topics" in
the documents, where a topic is a subject like "arts" or "education" that is discussed in the documents. The original setting in LDA, where each word has a topic label, may not work well with Twitter as tweets are short and a single tweet is more likely to talk about one topic. Hence, Twitter-LDA (T-LDA) has been proposed to address this issue. T-LDA also addresses the noisy nature of tweets, where it captures background words in tweets. As experiments in [7] have shown that T-LDA could capture more meaningful topics than LDA in Microblogs.
 
Data format:

1. Under /data/ folder you'll find three subfolders "Data4Model", "ModelRes" and "Tokens".

2. The data sets are tokenized and results are saved in "Tokens". The data are further processed for applying the model (Twitter-LDA), the resulting files are in "Data4Model". The model results are in "ModelRes" and the model parameter settings are in texts formatted as "modelParaemters-*.txt".

3. The model results are in the following format:
	
	3.1 Folder "TextWithLabel": this folder contains files with labeled results. Each file contains a set of posts correspond to the input file. Each line is post with labeled topic id, e.g: 2011-09-01:	z=156: beijing/156 olympic/156 opens/false. This means the post is with topic 156 (z = 156), it contains three terms, where "beijing" and "olympic" are with topic 156, "opens" is labeled as a background term (a term that is popular in many posts, similar to stop words);
	
	3.2 BackgroundWordsDistribution.txt: this file list top ranked background words;
	
	3.3 TopicCountsOnUsers.txt: this file contains N * T matrix, N is total number of input files (users), T is total number of topic, each element corresponds to number of times the user mentioned the topic;
	
	3.4 TopicsDistributionOnUsers.txt: The format is the same with the above file, but each line is a topic distribution of the user.
	

