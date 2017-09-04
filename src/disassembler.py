import opcodes

class Disassembler(object):
	"""Disassemble NES rom"""
	def __init__(self, rom):
		super(Disassembler, self).__init__()
		self.rom = rom 

	def disassemble(self):
		for byte in self.rom.getPrgRom():
			try:
				print self.parseOpCode(byte)
			except KeyError:
				print("invalid opcode")
		
	def parseOpCode(self,op):
		return opcodes.opcodeDict[op]

class ROM(object):

	def __init__(self, path):
		super(ROM, self).__init__()
		self.path = path 

		self.props = {}
		self._openROM(path)

	def _openROM(self,path):
		with open(path, 'rb') as f: 	
			self.parseROMInfo(f.read(16))


			if self.props["has_trainer"]:
				self._trainer = f.read(512)
			
			self._prgRom = f.read(self.props["prg_size"] * 16384)
			self._chrRom = f.read(self.props["chr_size"] * 8192)

	def getPrgRom(self):
		return self._prgRom

	def getChrRom(self):
		return self._chrRom		


	def parseROMInfo(self,romHeader):
		#for iNES format
		validStr = ''.join(romHeader[:4])
		# PRG ROM data (16384 * prg_size bytes)
		# CHR ROM data, if present (8192 * chr_size bytes)
		self.props["valid"] = True if validStr == "NES\x1A" else False
		if self.props["valid"] is not True:
			raise Exception("Invalid ROM file.")
		self.props["prg_size"] = ord(romHeader[4])
		self.props["chr_size"] = ord(romHeader[5])
		
		self._flag6 = self.byteToBinStr(romHeader[6])
		self._flag7 = self.byteToBinStr(romHeader[7])
		self._flag9 = self.byteToBinStr(romHeader[9])
		self._flag10 = self.byteToBinStr(romHeader[10])

		self.props["has_trainer"] = True if self._flag6[5] == '1' else False
		
		


		
	

	def byteToBinStr(self,byte):
		return '{0:08b}'.format(ord(byte))
if __name__ == '__main__':
	disassembler = Disassembler(ROM("../rom/tetris.nes"))
	disassembler.disassemble()
	# rom = ROM("../rom/tetris.nes")
	
	# print rom.props

	# print rom.getChrRom()
	# rom.getPrgRom()