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
 * Feel free to contact me if you find any
 * problems in the package.
 *
 * minghui.qiu.2010@smu.edu.sg
 * minghuiqiu@gmail.com
 *
*/

Brief introduction:

Latent Dirichlet Allocation (LDA) has been widely used in textual analysis. The original LDA is used to find hidden "topics" in
the documents, where a topic is a subject like "arts" or "education" that is discussed in the documents. The original setting in LDA, where each word has a topic label, may not work well with Twitter as tweets are short and a single tweet is more likely to talk about one topic. Hence, Twitter-LDA (T-LDA) has been proposed to address this issue. T-LDA also addresses the noisy nature of tweets, where it captures background words in tweets. Experiments have shown that T-LDA could capture more meaningful topics than LDA in Microblogs.

T-LDA has been widely used in many applications including aspect mining [1], user modeling [2], bursty topic detection[3], and keyphrase extraction [4].

[1] Aspect-Based Helpfulness Prediction for Online Product Reviews. Y Yang, C Chen, FS Bao, 2016 IEEE 28th International Conference on Tools with Artificial Intelligence (ICTAI), 2016. (http://ieeexplore.ieee.org/abstract/document/7814690/)

[2] It's Not What We Say But How We Say Them: LDA-based Behavior-Topic Model. Minghui Qiu, Feida Zhu and Jing Jiang. SDM'13.

[3] Finding bursty topics from microblogs. Qiming Diao, Jing Jiang, Feida Zhu and Ee-Peng Lim In Proceedings of the 50th Annual Meeting of the Association for Computational Linguistics, ACL'12.

[4] Topical keyphrase extraction from Twitter. [bib] Wayne Xin Zhao, Jing Jiang, Jing He, Yang Song, Palakorn Achanauparp, Ee-Peng Lim and Xiaoming Li In Proceedings of the 49th Annual Meeting of the Association for Computational Linguistics, ACL'11.


Data format:

1. Under /data/ folder you'll find three subfolders "Data4Model", "ModelRes" and "Tokens".

2. The data sets are tokenized and results are saved in "Tokens". The data are further processed for applying the model (Twitter-LDA), the resulting files are in "Data4Model". The model results are in "ModelRes" and the model parameter settings are in texts formatted as "modelParaemters-*.txt".

3. The model results are in the following format:

	3.1 Folder "TextWithLabel": this folder contains files with labeled results. Each file contains a set of posts correspond to the input file. Each line is post with labeled topic id, e.g: 2011-09-01:	z=156: beijing/156 olympic/156 opens/false. This means the post is with topic 156 (z = 156), it contains three terms, where "beijing" and "olympic" are with topic 156, "opens" is labeled as a background term (a term that is popular in many posts, similar to stop words);

	3.2 BackgroundWordsDistribution.txt: this file list top ranked background words;

	3.3 TopicCountsOnUsers.txt: this file contains N * T matrix, N is total number of input files (users), T is total number of topic, each element corresponds to number of times the user mentioned the topic;

	3.4 TopicsDistributionOnUsers.txt: The format is the same with the above file, but each line is a topic distribution of the user.

There are two ways to run the code:

1. You can create a new project using eclipse in the source code folder. Make sure all the jar files in the lib folder are loaded. Then just run this main file: TwitterLDA/TwitterLDAmain.java

2. If u are using ant, just run "ant build". To run the main file, cd to the source code folder, and run: `java -cp ../bin TwitterLDA/TwitterLDAmain`
