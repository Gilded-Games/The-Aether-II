package com.gildedgames.aether.client.models.entities.living;

import com.gildedgames.aether.client.renderer.ModelRendererAether;
import net.minecraft.entity.Entity;

/**
 * Moa_Wild - Undefined
 * Created using Tabula 7.0.1
 */
public class ModelMoaLodLow extends ModelMoaBase
{

	public ModelMoaLodLow()
	{
		this.textureWidth = 128;
		this.textureHeight = 256;
		this.HeadFeatherL2 = new ModelRendererAether(this, 56, 18);
		this.HeadFeatherL2.setRotationPoint(3.8F, 1.0F, 2.0F);
		this.HeadFeatherL2.addBox(-0.5F, -2.0F, -1.0F, 1, 4, 10, 0.0F);
		this.HeadBrow = new ModelRendererAether(this, 25, 32);
		this.HeadBrow.setRotationPoint(0.0F, -5.9F, -5.0F);
		this.HeadBrow.addBox(-3.5F, 0.0F, -3.5F, 7, 3, 7, 0.0F);
		this.setRotateAngle(HeadBrow, 0.0F, 0.7853981633974483F, 0.0F);
		this.HeadFeatherR1 = new ModelRendererAether(this, 0, 4);
		this.HeadFeatherR1.setRotationPoint(-3.8F, -4.0F, 2.0F);
		this.HeadFeatherR1.addBox(-0.5F, -2.0F, -1.0F, 1, 4, 10, 0.0F);
		this.HeadFeatherR2 = new ModelRendererAether(this, 0, 18);
		this.HeadFeatherR2.setRotationPoint(-3.8F, 1.0F, 1.9F);
		this.HeadFeatherR2.addBox(-0.5F, -2.0F, -1.0F, 1, 4, 10, 0.0F);
		this.LegRToeL = new ModelRendererAether(this, 34, 203);
		this.LegRToeL.setRotationPoint(1.0F, 1.2F, -2.5F);
		this.LegRToeL.addBox(-1.0F, -1.0F, -5.0F, 2, 2, 5, 0.0F);
		this.setRotateAngle(LegRToeL, 0.0F, -0.5235987755982988F, 0.0F);
		this.WingRFeatherExt1 = new ModelRendererAether(this, 0, 137);
		this.WingRFeatherExt1.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.WingRFeatherExt1.addBox(-17.0F, -1.0F, -2.5F, 17, 1, 5, 0.0F);
		this.setRotateAngle(WingRFeatherExt1, 0.0F, -0.17453292519943295F, 0.0F);
		this.HeadBack = new ModelRendererAether(this, 25, 0);
		this.HeadBack.setRotationPoint(0.0F, -6.0F, 3.5F);
		this.HeadBack.addBox(-4.0F, 0.0F, 0.0F, 8, 8, 6, 0.0F);
		this.setRotateAngle(HeadBack, -0.6283185307179586F, 0.0F, 0.0F);
		this.Neck = new ModelRendererAether(this, 96, 47);
		this.Neck.setRotationPoint(0.0F, 4.0F, -7.0F);
		this.Neck.addBox(-2.5F, -13.0F, -3.0F, 5, 17, 6, 0.0F);
		this.HeadFeatherL1 = new ModelRendererAether(this, 56, 4);
		this.HeadFeatherL1.setRotationPoint(3.8F, -4.0F, 2.0F);
		this.HeadFeatherL1.addBox(-0.5F, -2.0F, -1.0F, 1, 4, 10, 0.0F);
		this.BodyMain = new ModelRendererAether(this, 86, 92);
		this.BodyMain.setRotationPoint(0.0F, 1.1F, 0.0F);
		this.BodyMain.addBox(-4.5F, 0.0F, -5.0F, 9, 10, 12, 0.0F);
		this.WingR2 = new ModelRendererAether(this, 0, 102);
		this.WingR2.setRotationPoint(-8.0F, -1.3F, -1.0F);
		this.WingR2.addBox(-10.0F, -1.5F, -3.5F, 13, 3, 7, 0.0F);
		this.setRotateAngle(WingR2, 0.0F, 1.8325957145940461F, -0.5235987755982988F);
		this.JawMain = new ModelRendererAether(this, 90, 11);
		this.JawMain.setRotationPoint(0.0F, 2.0F, -2.0F);
		this.JawMain.addBox(-3.0F, -1.7F, -11.5F, 6, 4, 10, 0.0F);
		this.LegRFoot = new ModelRendererAether(this, 15, 196);
		this.LegRFoot.setRotationPoint(0.0F, 0.0F, -1.5F);
		this.LegRFoot.addBox(-2.0F, 0.0F, -3.5F, 4, 2, 5, 0.0F);
		this.setRotateAngle(LegRFoot, -0.593411945678072F, 0.0F, 0.0F);
		this.LegL2 = new ModelRendererAether(this, 64, 165);
		this.LegL2.setRotationPoint(1.5F, 7.0F, 1.5F);
		this.LegL2.addBox(-2.0F, -1.0F, -2.0F, 4, 9, 4, 0.0F);
		this.setRotateAngle(LegL2, 1.9198621771937625F, 0.0F, 0.0F);
		this.WingL1 = new ModelRendererAether(this, 40, 94);
		this.WingL1.setRotationPoint(2.5F, 0.0F, 0.0F);
		this.WingL1.addBox(-2.5F, -2.0F, -4.0F, 10, 2, 6, 0.0F);
		this.WingLFeatherExt2 = new ModelRendererAether(this, 44, 131);
		this.WingLFeatherExt2.setRotationPoint(0.0F, 0.5F, 2.0F);
		this.WingLFeatherExt2.addBox(-2.0F, -1.0F, -2.5F, 16, 1, 5, 0.0F);
		this.setRotateAngle(WingLFeatherExt2, 0.0F, -0.17453292519943295F, 0.0F);
		this.LegRToeM = new ModelRendererAether(this, 14, 203);
		this.LegRToeM.setRotationPoint(0.0F, 0.8F, -2.5F);
		this.LegRToeM.addBox(-1.5F, -1.0F, -7.0F, 3, 2, 7, 0.0F);
		this.WingRFeatherInt1 = new ModelRendererAether(this, 10, 119);
		this.WingRFeatherInt1.setRotationPoint(-2.0F, 0.0F, 3.0F);
		this.WingRFeatherInt1.addBox(-11.5F, 0.0F, -2.5F, 12, 1, 5, 0.0F);
		this.setRotateAngle(WingRFeatherInt1, 0.0F, 1.3962634015954636F, 0.0F);
		this.WingLFeatherExt1 = new ModelRendererAether(this, 44, 137);
		this.WingLFeatherExt1.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.WingLFeatherExt1.addBox(0.0F, -1.0F, -2.5F, 17, 1, 5, 0.0F);
		this.setRotateAngle(WingLFeatherExt1, 0.0F, 0.17453292519943295F, 0.0F);
		this.TailFeatherL = new ModelRendererAether(this, 70, 230);
		this.TailFeatherL.setRotationPoint(3.0F, 1.0F, -1.0F);
		this.TailFeatherL.addBox(-2.5F, -1.0F, 0.0F, 5, 2, 20, 0.0F);
		this.setRotateAngle(TailFeatherL, -0.5235987755982988F, 1.5707963267948966F, 0.0F);
		this.LegLFoot = new ModelRendererAether(this, 63, 196);
		this.LegLFoot.setRotationPoint(0.0F, 0.0F, -1.5F);
		this.LegLFoot.addBox(-2.0F, 0.0F, -3.5F, 4, 2, 5, 0.0F);
		this.setRotateAngle(LegLFoot, -0.593411945678072F, 0.0F, 0.0F);
		this.WingLFeatherInt1 = new ModelRendererAether(this, 44, 119);
		this.WingLFeatherInt1.setRotationPoint(2.0F, 0.0F, 3.0F);
		this.WingLFeatherInt1.addBox(-0.5F, 0.0F, -2.5F, 12, 1, 5, 0.0F);
		this.setRotateAngle(WingLFeatherInt1, 0.0F, -1.3962634015954636F, 0.0F);
		this.BodyFront = new ModelRendererAether(this, 87, 70);
		this.BodyFront.setRotationPoint(0.0F, -0.5F, -4.0F);
		this.BodyFront.addBox(-4.0F, 0.0F, -9.0F, 8, 10, 12, 0.0F);
		this.setRotateAngle(BodyFront, -0.17453292519943295F, 0.0F, 0.0F);
		this.WingLFeatherExt3 = new ModelRendererAether(this, 44, 125);
		this.WingLFeatherExt3.setRotationPoint(-2.0F, 1.0F, 3.0F);
		this.WingLFeatherExt3.addBox(-0.5F, -1.0F, -2.5F, 14, 1, 5, 0.0F);
		this.setRotateAngle(WingLFeatherExt3, 0.0F, -0.6981317007977318F, 0.0F);
		this.WingR1 = new ModelRendererAether(this, 8, 94);
		this.WingR1.setRotationPoint(-2.5F, 0.0F, 0.0F);
		this.WingR1.addBox(-7.5F, -2.0F, -4.0F, 10, 2, 6, 0.0F);
		this.WingL2 = new ModelRendererAether(this, 40, 102);
		this.WingL2.setRotationPoint(8.0F, -1.3F, -1.0F);
		this.WingL2.addBox(-3.0F, -1.5F, -3.5F, 13, 3, 7, 0.0F);
		this.setRotateAngle(WingL2, 0.0F, -1.8325957145940461F, 0.5235987755982988F);
		this.WingRFeatherExt3 = new ModelRendererAether(this, 6, 125);
		this.WingRFeatherExt3.setRotationPoint(2.0F, 1.0F, 3.0F);
		this.WingRFeatherExt3.addBox(-12.5F, -1.0F, -2.5F, 14, 1, 5, 0.0F);
		this.setRotateAngle(WingRFeatherExt3, 0.0F, 0.6981317007977318F, 0.0F);
		this.HeadBeakIntL = new ModelRendererAether(this, 120, 11);
		this.HeadBeakIntL.setRotationPoint(3.5F, 0.0F, -4.5F);
		this.HeadBeakIntL.addBox(0.0F, 0.0F, -3.0F, 0, 4, 3, 0.0F);
		this.setRotateAngle(HeadBeakIntL, 0.0F, 0.5235987755982988F, 0.0F);
		this.LegLAnkle = new ModelRendererAether(this, 65, 189);
		this.LegLAnkle.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.LegLAnkle.addBox(-1.0F, -1.0F, -3.0F, 2, 2, 5, 0.0F);
		this.setRotateAngle(LegLAnkle, 0.4363323129985824F, 0.0F, 0.3665191429188092F);
		this.LegRAnkle = new ModelRendererAether(this, 17, 189);
		this.LegRAnkle.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.LegRAnkle.addBox(-1.0F, -1.0F, -3.0F, 2, 2, 5, 0.0F);
		this.setRotateAngle(LegRAnkle, 0.4363323129985824F, 0.0F, -0.3665191429188092F);
		this.BodyBack = new ModelRendererAether(this, 97, 114);
		this.BodyBack.setRotationPoint(0.0F, 10.0F, 7.0F);
		this.BodyBack.addBox(-4.0F, -6.0F, -2.0F, 8, 6, 2, 0.0F);
		this.setRotateAngle(BodyBack, -0.41887902047863906F, 0.0F, 0.0F);
		this.LegR2 = new ModelRendererAether(this, 16, 165);
		this.LegR2.setRotationPoint(-1.5F, 7.0F, 1.5F);
		this.LegR2.addBox(-2.0F, -1.0F, -2.0F, 4, 9, 4, 0.0F);
		this.setRotateAngle(LegR2, 1.9198621771937625F, 0.0F, 0.0F);
		this.WingR3 = new ModelRendererAether(this, 0, 0);
		this.WingR3.setRotationPoint(-9.0F, 0.0F, 0.0F);
		this.WingR3.addBox(1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
		this.setRotateAngle(WingR3, 0.0F, 0.2617993877991494F, -0.17453292519943295F);
		this.HeadBeakMain = new ModelRendererAether(this, 25, 54);
		this.HeadBeakMain.setRotationPoint(0.0F, -2.0F, -4.7F);
		this.HeadBeakMain.addBox(-2.5F, -3.8F, -9.4F, 5, 6, 9, 0.0F);
		this.TailFeatherM = new ModelRendererAether(this, 36, 230);
		this.TailFeatherM.setRotationPoint(1.0F, 0.0F, 1.0F);
		this.TailFeatherM.addBox(-2.5F, -1.0F, 0.0F, 5, 2, 24, 0.0F);
		this.setRotateAngle(TailFeatherM, -0.5235987755982988F, 0.7853981633974483F, 0.0F);
		this.LegL1 = new ModelRendererAether(this, 60, 146);
		this.LegL1.setRotationPoint(2.5F, 6.0F, 3.0F);
		this.LegL1.addBox(-1.0F, -2.5F, -3.5F, 5, 12, 7, 0.0F);
		this.setRotateAngle(LegL1, -0.6981317007977318F, 0.0F, -0.3490658503988659F);
		this.HeadFront = new ModelRendererAether(this, 27, 42);
		this.HeadFront.setRotationPoint(0.0F, 0.5F, -4.5F);
		this.HeadFront.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
		this.setRotateAngle(HeadFront, 0.0F, 0.7853981633974483F, 0.0F);
		this.TailBase = new ModelRendererAether(this, 91, 122);
		this.TailBase.setRotationPoint(0.0F, 1.7F, 8.5F);
		this.TailBase.addBox(-4.5F, -1.5F, -4.5F, 8, 5, 8, 0.0F);
		this.setRotateAngle(TailBase, 0.0F, -0.7853981633974483F, 0.0F);
		this.LegRToeR = new ModelRendererAether(this, 0, 203);
		this.LegRToeR.setRotationPoint(-1.0F, 1.2F, -2.5F);
		this.LegRToeR.addBox(-1.0F, -1.0F, -5.0F, 2, 2, 5, 0.0F);
		this.setRotateAngle(LegRToeR, 0.0F, 0.5235987755982988F, 0.0F);
		this.LegL3 = new ModelRendererAether(this, 66, 178);
		this.LegL3.setRotationPoint(0.0F, 6.5F, 0.0F);
		this.LegL3.addBox(-1.5F, -1.0F, -1.5F, 3, 8, 3, 0.0F);
		this.setRotateAngle(LegL3, -1.0471975511965976F, 0.0F, 0.0F);
		this.HeadMain = new ModelRendererAether(this, 22, 14);
		this.HeadMain.setRotationPoint(0.0F, -12.0F, 0.0F);
		this.HeadMain.addBox(-4.5F, -6.0F, -4.5F, 9, 10, 8, 0.0F);
		this.setRotateAngle(HeadMain, 0.17453292519943295F, 0.0F, 0.0F);
		this.JawToothL2 = new ModelRendererAether(this, 120, 4);
		this.JawToothL2.setRotationPoint(2.3F, -1.5F, -9.6F);
		this.JawToothL2.addBox(-0.5F, -1.0F, -1.0F, 1, 2, 2, 0.0F);
		this.setRotateAngle(JawToothL2, 0.7853981633974483F, 0.0F, -0.17453292519943295F);
		this.WingL3 = new ModelRendererAether(this, 0, 0);
		this.WingL3.setRotationPoint(9.0F, 0.0F, 0.0F);
		this.WingL3.addBox(-3.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
		this.setRotateAngle(WingL3, 0.0F, -0.2617993877991494F, 0.17453292519943295F);
		this.LegR3 = new ModelRendererAether(this, 18, 178);
		this.LegR3.setRotationPoint(0.0F, 6.5F, 0.0F);
		this.LegR3.addBox(-1.5F, -1.0F, -1.5F, 3, 8, 3, 0.0F);
		this.setRotateAngle(LegR3, -1.0471975511965976F, 0.0F, 0.0F);
		this.ShoulderR = new ModelRendererAether(this, 10, 81);
		this.ShoulderR.setRotationPoint(-3.0F, 1.5F, -4.0F);
		this.ShoulderR.addBox(-4.5F, -2.5F, -4.5F, 6, 4, 9, 0.0F);
		this.setRotateAngle(ShoulderR, 0.0F, 0.0F, -0.9599310885968813F);
		this.WingLFeatherInt2 = new ModelRendererAether(this, 44, 113);
		this.WingLFeatherInt2.setRotationPoint(6.0F, -1.5F, 0.5F);
		this.WingLFeatherInt2.addBox(-0.5F, 0.0F, -2.5F, 11, 1, 5, 0.0F);
		this.setRotateAngle(WingLFeatherInt2, 0.0F, -1.9198621771937625F, 0.0F);
		this.JawToothL3 = new ModelRendererAether(this, 120, 8);
		this.JawToothL3.setRotationPoint(2.3F, -1.5F, -6.5F);
		this.JawToothL3.addBox(-0.5F, -1.0F, -1.0F, 1, 2, 2, 0.0F);
		this.setRotateAngle(JawToothL3, 0.7853981633974483F, 0.0F, -0.17453292519943295F);
		this.ShoulderL = new ModelRendererAether(this, 40, 81);
		this.ShoulderL.setRotationPoint(3.0F, 1.5F, -4.0F);
		this.ShoulderL.addBox(-1.5F, -2.5F, -4.5F, 6, 4, 9, 0.0F);
		this.setRotateAngle(ShoulderL, 0.0F, 0.0F, 0.9599310885968813F);
		this.WingRFeatherExt2 = new ModelRendererAether(this, 2, 131);
		this.WingRFeatherExt2.setRotationPoint(0.0F, 0.5F, 2.0F);
		this.WingRFeatherExt2.addBox(-14.0F, -1.0F, -2.5F, 16, 1, 5, 0.0F);
		this.setRotateAngle(WingRFeatherExt2, 0.0F, 0.17453292519943295F, 0.0F);
		this.WingRFeatherInt2 = new ModelRendererAether(this, 12, 113);
		this.WingRFeatherInt2.setRotationPoint(-6.0F, -1.5F, 0.5F);
		this.WingRFeatherInt2.addBox(-11.5F, 0.0F, -2.5F, 11, 1, 5, 0.0F);
		this.setRotateAngle(WingRFeatherInt2, 0.0F, 1.7453292519943295F, 0.0F);
		this.LegLToeM = new ModelRendererAether(this, 62, 203);
		this.LegLToeM.setRotationPoint(0.0F, 1.2F, -2.5F);
		this.LegLToeM.addBox(-1.5F, -1.0F, -7.0F, 3, 2, 7, 0.0F);
		this.LegLToeL = new ModelRendererAether(this, 82, 203);
		this.LegLToeL.setRotationPoint(1.0F, 0.8F, -2.5F);
		this.LegLToeL.addBox(-1.0F, -1.0F, -5.0F, 2, 2, 5, 0.0F);
		this.setRotateAngle(LegLToeL, 0.0F, -0.5235987755982988F, 0.0F);
		this.HeadBeakIntR = new ModelRendererAether(this, 86, 11);
		this.HeadBeakIntR.setRotationPoint(-3.5F, 0.0F, -4.5F);
		this.HeadBeakIntR.addBox(0.0F, 0.0F, -3.0F, 0, 4, 3, 0.0F);
		this.setRotateAngle(HeadBeakIntR, 0.0F, -0.5235987755982988F, 0.0F);
		this.LegLToeR = new ModelRendererAether(this, 48, 203);
		this.LegLToeR.setRotationPoint(-1.0F, 0.8F, -2.5F);
		this.LegLToeR.addBox(-1.0F, -1.0F, -5.0F, 2, 2, 5, 0.0F);
		this.setRotateAngle(LegLToeR, 0.0F, 0.5235987755982988F, 0.0F);
		this.LegR1 = new ModelRendererAether(this, 12, 146);
		this.LegR1.setRotationPoint(-2.5F, 6.0F, 3.0F);
		this.LegR1.addBox(-4.0F, -2.5F, -3.5F, 5, 12, 7, 0.0F);
		this.setRotateAngle(LegR1, -0.6981317007977318F, 0.0F, 0.3490658503988659F);
		this.TailFeatherR = new ModelRendererAether(this, 10, 230);
		this.TailFeatherR.setRotationPoint(-1.0F, 1.0F, 3.0F);
		this.TailFeatherR.addBox(-2.5F, -1.0F, 0.0F, 5, 2, 20, 0.0F);
		this.setRotateAngle(TailFeatherR, -0.5235987755982988F, 0.0F, 0.0F);
		this.HeadMain.addChild(this.HeadFeatherL2);
		this.HeadMain.addChild(this.HeadBrow);
		this.HeadMain.addChild(this.HeadFeatherR1);
		this.HeadMain.addChild(this.HeadFeatherR2);
		this.LegRFoot.addChild(this.LegRToeL);
		this.WingR3.addChild(this.WingRFeatherExt1);
		this.HeadMain.addChild(this.HeadBack);
		this.BodyFront.addChild(this.Neck);
		this.HeadMain.addChild(this.HeadFeatherL1);
		this.WingR1.addChild(this.WingR2);
		this.HeadMain.addChild(this.JawMain);
		this.LegRAnkle.addChild(this.LegRFoot);
		this.LegL1.addChild(this.LegL2);
		this.ShoulderL.addChild(this.WingL1);
		this.WingL3.addChild(this.WingLFeatherExt2);
		this.LegRFoot.addChild(this.LegRToeM);
		this.WingR2.addChild(this.WingRFeatherInt1);
		this.WingL3.addChild(this.WingLFeatherExt1);
		this.TailBase.addChild(this.TailFeatherL);
		this.LegLAnkle.addChild(this.LegLFoot);
		this.WingL2.addChild(this.WingLFeatherInt1);
		this.BodyMain.addChild(this.BodyFront);
		this.WingL3.addChild(this.WingLFeatherExt3);
		this.ShoulderR.addChild(this.WingR1);
		this.WingL1.addChild(this.WingL2);
		this.WingR3.addChild(this.WingRFeatherExt3);
		this.HeadMain.addChild(this.HeadBeakIntL);
		this.LegL3.addChild(this.LegLAnkle);
		this.LegR3.addChild(this.LegRAnkle);
		this.BodyMain.addChild(this.BodyBack);
		this.LegR1.addChild(this.LegR2);
		this.WingR2.addChild(this.WingR3);
		this.HeadMain.addChild(this.HeadBeakMain);
		this.TailBase.addChild(this.TailFeatherM);
		this.BodyMain.addChild(this.LegL1);
		this.HeadMain.addChild(this.HeadFront);
		this.BodyMain.addChild(this.TailBase);
		this.LegRFoot.addChild(this.LegRToeR);
		this.LegL2.addChild(this.LegL3);
		this.Neck.addChild(this.HeadMain);
		this.JawMain.addChild(this.JawToothL2);
		this.WingL2.addChild(this.WingL3);
		this.LegR2.addChild(this.LegR3);
		this.BodyFront.addChild(this.ShoulderR);
		this.WingL1.addChild(this.WingLFeatherInt2);
		this.JawMain.addChild(this.JawToothL3);
		this.BodyFront.addChild(this.ShoulderL);
		this.WingR3.addChild(this.WingRFeatherExt2);
		this.WingR1.addChild(this.WingRFeatherInt2);
		this.LegLFoot.addChild(this.LegLToeM);
		this.LegLFoot.addChild(this.LegLToeL);
		this.HeadMain.addChild(this.HeadBeakIntR);
		this.LegLFoot.addChild(this.LegLToeR);
		this.BodyMain.addChild(this.LegR1);
		this.TailBase.addChild(this.TailFeatherR);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.BodyMain.render(f5);
	}
}