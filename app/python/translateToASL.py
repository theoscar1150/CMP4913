#!/usr/bin/env python
# coding: utf-8

# This code takes in a sentence and parses it to the correct grammatical form then plays the correct videos needed
import string
# import spacy
import itertools
import os
# import numpy as np

def translateToASL(sentence):
    
    # nlp = spacy.load('en_core_web_sm')
    sentence = [list(x) for x in sentence.split()]
    sentence = [x for y in sentence for x in y]
    #sentence = [x+'.mp4' for x in sentence]
    return sentence
 #
 #  timeOrderWords = ['aurora', 'afternoon','bedtime', 'break of day', 'break of the day', 'breakfast time','canonical hour','closing time',
 # 'cockcrow','complin','compline','crepuscle','crepuscule','dawn','dawning','daybreak','dayspring','dinnertime',
 # 'dusk','early morning hour','evenfall','evening','evensong','fall','first light','gloam','gloaming','happy hour','high noon','late night hour',
 # 'lights out','lunch period','lunchtime','matins','mealtime','midday','midnight','morning','morning prayer','night','nightfall','none','nones','noon',
 # 'noonday','noontide','prime','rush hour','sext','small hours','sundown','sunrise','sunset','sunup','suppertime','terce','tierce','twelve noon','twilight','vespers','zero hour']
 #  doc = nlp(sentence)
 #  # for word in doc
 #  tokens_list = []
 #  signs_list = []
 #  for token in doc:
 #    # remove auxilaries, punctuation, determiners, prepositions
 #      if token.pos_ not in ['AUX', 'PUNCT'] and token.tag_ not in ['DT','IN']:
 #          tokens_list.append([token.text, token.lemma_, token.pos_, token.tag_,])
 #          signs_list.append(token.lemma_)
 #
 #  for sign in signs_list:
 #    signs_list[signs_list.index(sign)] = sign.lower()
 #  # print(signs_list)
 #
 #  if 'not' in signs_list:
 #    ind = signs_list.index('not')
 #    signs_list[ind], signs_list[ind+1] = signs_list[ind+1], signs_list[ind]
 #  # not comes after the verb it negates in ASL
 #  # print(signs_list)
 #
 #  if 'i' in signs_list:
 #    ind = signs_list.index('i')
 #    signs_list[ind] = 'me'
 #  # 'me' is used instead of 'i' in ASL
 #  # print(signs_list)
 #
 #  for sign in signs_list:
 #    if sign in timeOrderWords:
 #      word = signs_list[signs_list.index(sign)]
 #      signs_list.remove(word)
 #      signs_list.insert(0, word)
 #  # time order words need to be at the start of the sentence in ASL
 #  return signs_list

