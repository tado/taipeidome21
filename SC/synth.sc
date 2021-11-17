(
SynthDef("simpleSine", {
	arg freq = 440, mul = 0, detune=1.01;
	var out;
	out = SinOsc.ar([freq, freq*detune], pi.rand2, mul);
	Out.ar(3, out);
}).store;

SynthDef("fx", {
	var in, out;
	in = In.ar(3, 2);
	out = LeakDC.ar(in);
	/*
	out = Compander.ar(out, out,
		thresh:0.1,
		slopeBelow: 1,
		slopeAbove: 0.1,
		clampTime: 0.01,
		relaxTime: 0.01,
		mul: 0.8);
		*/
	//out = GVerb.ar(out, roomsize: 20, revtime: 3);

	Out.ar(0, out);
}).store;
SynthDef("simpleSine", {
	arg freq = 440, mul = 0, detune=1.02;
	var out;
	out = SinOsc.ar([freq, freq*detune], pi.rand2, mul);
	Out.ar(3, out);
}).store;

SynthDef("sinefx", {
	arg amp = 0.5;
	var out;
	out = In.ar(3, 2);
	//out = Formlet.ar(out, LFNoise1.kr(2.0, 400, 1000), 0.001, 0.01);
	//out = RLPF.ar(out, LFNoise1.kr(0.2, 500, 1100), 0.4);
	8.do({ out = AllpassL.ar(out, 0.1, LFNoise2.kr([rrand(0.0, 0.01),rrand(0.0, 0.01)],0.01,0.06), 1.0) });
	out = out * amp;
	Out.ar(0, out);
}).store;

SynthDef("col_closesaw", {
	arg fadeTime = 30, n = 0, rq = 0.3, detune = 0.001, base = 20, ratio = 1.5, harm = 1.5, amp = 0.2, gate=0;
	var lfo, env, out;
	env = EnvGen.kr(Env.new([0,1], [fadeTime], 'sine'));
	lfo = SinOsc.ar(rrand(0.03, 0.05), 0, 100, 600);
	out = Saw.ar([base+detune.rand, base+detune.rand] * (ratio ** n)) * amp
	+ Saw.ar([base*harm+detune.rand, base*harm+detune.rand] * (ratio ** n)) * amp;
	out = out * env;
	out = RLPF.ar(out, lfo * (1.5 ** n), rq).clip2 * 0.5;
	out = out * EnvGen.kr(Env.adsr(releaseTime:10), gate, doneAction: 2);
	Out.ar(5, out);
}).add;

SynthDef("col_closefx", {
	arg lpf=440, rq=0.5, amp=0.5;
	var in, out;
	in = In.ar(5, 2);
	4.do({ in = AllpassL.ar(in, 0.1, LFNoise2.kr([rrand(0.0, 0.1),rrand(0.0, 0.1)],0.01,0.06), 0.5) });
	//32.do({ in = AllpassL.ar(in, 0.1, LFNoise2.kr([rrand(0.0, 0.1),rrand(0.0, 0.1)],0.01,0.06), 2.0) });
	//out = CompanderD.ar(in) * amp;
	//out = CompanderD.ar(MidEQ.ar(in, 80, 0.8, 10) * amp);
	out = MidEQ.ar(in, 50, 1.0, 12) * amp;
	Out.ar(0, out);
	//Out.ar(7, out);
}).add;

SynthDef("col_comb", {
	arg inamp = 0.0, amp = 1.0, delaytime = 0.05;
	var out;
	out = (In.ar(5, 2) + In.ar(3, 2)) * inamp;
	out = CombL.ar(out, 1.0,  delaytime, 5);
	//in = Pan2.ar(in, LFNoise0.kr(1));
	//out = out * amp;
	out = (Limiter.ar(out)).softclip * amp;
	Out.ar(0, out);
}).store;
)

(
SynthDef("col_closesaw", {
	arg fadeTime = 30, n = 0, rq = 0.3, detune = 0.001, base = 20, ratio = 1.5, harm = 1.5, amp = 0.2, gate=1;
	var lfo, env, out;
	env = EnvGen.kr(Env.new([0,1], [fadeTime], 'sine'));
	lfo = SinOsc.ar(rrand(0.03, 0.05), 0, 100, 600);
	out = Saw.ar([base+detune.rand, base+detune.rand] * (ratio ** n)) * amp
	+ Saw.ar([base*harm+detune.rand, base*harm+detune.rand] * (ratio ** n)) * amp;
	out = out * env;
	out = RLPF.ar(out, lfo * (1.5 ** n), rq).clip2 * 0.5;
	//out = out * EnvGen.kr(Env.adsr(releaseTime:10), gate, doneAction: 2);
	Out.ar(8, out);
}).add;

SynthDef("col_closefx", {
	arg lpf=440, rq=0.5, amp=0.5;
	var in, out;
	in = In.ar(8, 2);
    in = NHHall.ar(in);
	//4.do({ in = AllpassL.ar(in, 0.1, LFNoise2.kr([rrand(0.0, 0.1),rrand(0.0, 0.1)],0.01,0.06), 0.5) });
	//32.do({ in = AllpassL.ar(in, 0.1, LFNoise2.kr([rrand(0.0, 0.1),rrand(0.0, 0.1)],0.01,0.06), 2.0) });
	//out = CompanderD.ar(in) * amp;
	//out = CompanderD.ar(MidEQ.ar(in, 80, 0.8, 10) * amp);
	out = MidEQ.ar(in, 50, 1.0, 12) * amp;
	Out.ar(0, out);
	//Out.ar(7, out);
}).add;
)

{SinOsc.ar().dup}.play

Synth("col_closefx")

Synth("col_closesaw", ["n", 13, "gate", 1, "rq", 1.0, "amp", 0.75, "detune", 0.02])