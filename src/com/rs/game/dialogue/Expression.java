package com.rs.game.dialogue;

public interface Expression {

	// Happy expressions
	short head_side2side_happy = 9843;
	short happy_plain = 9843;
	short happy_plain_eyebrows_up = 9847;
	short happy = 9850;

	// Sad expressions
	short sad = 9761;
	short sad_crying = 9764;
	short dispair = 9767;
	short saddest = 9768;
	short guilty_fear = 9769; // same as sad1, but less head movement and eyes look up, like seeking forgiveness "aww :("

	// Angry expressions
	short pissed_off = 9781;
	short angry_2 = 9784; // same as angry1 but guy doesn't talk as loudly and eyebrows not as cross
	short annoyed = 9785;
	short more_pissed_off = 9788;
	short most_pissed_off = 9789;

	// Fear expressions
	short fear_response_blinking = 9773;
	short scared = 9775;
	short fear_response_catatonic = 9776;
	short holy_shit = 9777;
	short afraid = 9780;

	// Surprise expressions
	short shock_omg = 9750;
	short shock_guilty = 9753;

	// Thankful expression
	short plain_thank_you = 9757;

	// Miscellaneous expressions
	short little_side2side_head = 9746;
	short bumping_up_down_agreeing = 9793;
	short sleeping = 9802;
	short plain_talking = 9803;
	short plain_talking_with_blink = 9804;
	short talks_looksatplayer_looksback = 9806;
	short muttering_thinking = 9811;
	short question = 9827;
	short thinking_plain = 9830;
	short rolls_eyes = 9831;
	short eyes_side2side_reading = 9834;
	short drunk_happy_tired = 9835;
	short suspicious = 9836;
	short laugh_happy = 9840;
	short hearty_laugh = 9841;
	short evil_laugh = 9842;
	short conceited_laugh = 9851;
	short praying = 9877;
	short stunned_head_rolling = 9878;
}