/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ut3converter2.export.utx;

import java.util.ArrayList;

/**
 *
 * @author Hyperion
 */
public class UTPackagesNatives {

    static final int nffFunction = 0;
    static final int nffPreOperator = 1;
    static final int nffPostOperator = 2;
    static final int nffOperator = 3;
    static TNativeFunctions NativeFunctions_UT=getNativeFunctions_UT();


    public UTPackagesNatives() {
    }

    public static class TNativeFunction
    {
        long index;
        int Format;
        int OperatorPrecedence;
        String Name;

        public TNativeFunction(long index, int Format, int OperatorPrecedence, String Name) {
            this.index = index;
            this.Format = Format;
            this.OperatorPrecedence = OperatorPrecedence;
            this.Name = Name;
        }

        
    }

    public static class TNativeFunctions
    {
        ArrayList <TNativeFunction>al;

        public TNativeFunctions(ArrayList<TNativeFunction> al) {
            this.al = al;
        }
    }

    public static TNativeFunctions getNativeFunctions_UT()
    {
        ArrayList al2 = new ArrayList();
        al2.add(new TNativeFunction(112, nffOperator, 040, "$"));
        al2.add(new TNativeFunction(113, nffFunction, 0, "GotoState")); // Core.u
        al2.add(new TNativeFunction(114, nffOperator, 24, "==")); // Core.u
        al2.add(new TNativeFunction(115, nffOperator, 24, "<")); // Core.u
        al2.add(new TNativeFunction(116, nffOperator, 24, ">")); // Core.u
        al2.add(new TNativeFunction(117, nffFunction, 0, "Enable")); // Core.u
        al2.add(new TNativeFunction(118, nffFunction, 0, "Disable")); // Core.u
        al2.add(new TNativeFunction(119, nffOperator, 26, "!=")); // Core.u
        al2.add(new TNativeFunction(120, nffOperator, 24, "<=")); // Core.u
        al2.add(new TNativeFunction(121, nffOperator, 24, ">=")); // Core.u
        al2.add(new TNativeFunction(122, nffOperator, 24, "==")); // Core.u
        al2.add(new TNativeFunction(123, nffOperator, 26, "!=")); // Core.u
        al2.add(new TNativeFunction(124, nffOperator, 24, "~=")); // Core.u
        al2.add(new TNativeFunction(125, nffFunction, 0, "Len")); // Core.u
        al2.add(new TNativeFunction(126, nffFunction, 0, "InStr")); // Core.u
        al2.add(new TNativeFunction(127, nffFunction, 0, "Mid")); // Core.u
        al2.add(new TNativeFunction(128, nffFunction, 0, "Left")); // Core.u
        al2.add(new TNativeFunction(129, nffPreOperator, 0, "!")); // Core.u
        al2.add(new TNativeFunction(130, nffOperator, 10, "&&")); // Core.u
        al2.add(new TNativeFunction(131, nffOperator, 10, "^^")); // Core.u
        al2.add(new TNativeFunction(132, nffOperator, 11, "||")); // Core.u
        al2.add(new TNativeFunction(133, nffOperator, 34, "*=")); // Core.u
        al2.add(new TNativeFunction(134, nffOperator, 34, "/=")); // Core.u
        al2.add(new TNativeFunction(135, nffOperator, 34, "+=")); // Core.u
        al2.add(new TNativeFunction(136, nffOperator, 34, "-=")); // Core.u
        al2.add(new TNativeFunction(137, nffPreOperator, 0, "++")); // Core.u
        al2.add(new TNativeFunction(138, nffPreOperator, 0, "--")); // Core.u
        al2.add(new TNativeFunction(139, nffPostOperator, 0, "++")); // Core.u
        al2.add(new TNativeFunction(140, nffPostOperator, 0, "--")); // Core.u
        al2.add(new TNativeFunction(141, nffPreOperator, 0, "~")); // Core.u
        al2.add(new TNativeFunction(142, nffOperator, 24, "==")); // Core.u
        al2.add(new TNativeFunction(143, nffPreOperator, 0, "-")); // Core.u
        al2.add(new TNativeFunction(144, nffOperator, 16, "*")); // Core.u
        al2.add(new TNativeFunction(145, nffOperator, 16, "/")); // Core.u
        al2.add(new TNativeFunction(146, nffOperator, 20, "+")); // Core.u
        al2.add(new TNativeFunction(147, nffOperator, 20, "-")); // Core.u
        al2.add(new TNativeFunction(148, nffOperator, 22, "<<")); // Core.u
        al2.add(new TNativeFunction(149, nffOperator, 22, ">>")); // Core.u
        al2.add(new TNativeFunction(150, nffOperator, 24, "<")); // Core.u
        al2.add(new TNativeFunction(151, nffOperator, 24, ">")); // Core.u
        al2.add(new TNativeFunction(152, nffOperator, 24, "<=")); // Core.u
        al2.add(new TNativeFunction(153, nffOperator, 24, ">=")); // Core.u
        al2.add(new TNativeFunction(154, nffOperator, 24, "==")); // Core.u
        al2.add(new TNativeFunction(155, nffOperator, 26, "!=")); // Core.u
        al2.add(new TNativeFunction(156, nffOperator, 28, "&")); // Core.u
        al2.add(new TNativeFunction(157, nffOperator, 28, "^")); // Core.u
        al2.add(new TNativeFunction(158, nffOperator, 28, "|")); // Core.u
        al2.add(new TNativeFunction(159, nffOperator, 34, "*=")); // Core.u
        al2.add(new TNativeFunction(160, nffOperator, 34, "/=")); // Core.u
        al2.add(new TNativeFunction(161, nffOperator, 34, "+=")); // Core.u
        al2.add(new TNativeFunction(162, nffOperator, 34, "-=")); // Core.u
        al2.add(new TNativeFunction(163, nffPreOperator, 0, "++")); // Core.u
        al2.add(new TNativeFunction(164, nffPreOperator, 0, "--")); // Core.u
        al2.add(new TNativeFunction(165, nffPostOperator, 0, "++")); // Core.u
        al2.add(new TNativeFunction(166, nffPostOperator, 0, "--")); // Core.u
        al2.add(new TNativeFunction(167, nffFunction, 0, "Rand")); // Core.u
        al2.add(new TNativeFunction(168, nffOperator, 40, "@")); // Core.u
        al2.add(new TNativeFunction(169, nffPreOperator, 0, "-")); // Core.u
        al2.add(new TNativeFunction(170, nffOperator, 12, "**")); // Core.u
        al2.add(new TNativeFunction(171, nffOperator, 16, "*")); // Core.u
        al2.add(new TNativeFunction(172, nffOperator, 16, "/")); // Core.u
        al2.add(new TNativeFunction(173, nffOperator, 18, "%")); // Core.u
        al2.add(new TNativeFunction(174, nffOperator, 20, "+")); // Core.u
        al2.add(new TNativeFunction(175, nffOperator, 20, "-")); // Core.u
        al2.add(new TNativeFunction(176, nffOperator, 24, "<")); // Core.u
        al2.add(new TNativeFunction(177, nffOperator, 24, ">")); // Core.u
        al2.add(new TNativeFunction(178, nffOperator, 24, "<=")); // Core.u
        al2.add(new TNativeFunction(179, nffOperator, 24, ">=")); // Core.u
        al2.add(new TNativeFunction(180, nffOperator, 24, "==")); // Core.u
        al2.add(new TNativeFunction(181, nffOperator, 26, "!=")); // Core.u
        al2.add(new TNativeFunction(182, nffOperator, 34, "*=")); // Core.u
        al2.add(new TNativeFunction(183, nffOperator, 34, "/=")); // Core.u
        al2.add(new TNativeFunction(184, nffOperator, 34, "+=")); // Core.u
        al2.add(new TNativeFunction(185, nffOperator, 34, "-=")); // Core.u
        al2.add(new TNativeFunction(186, nffFunction, 0, "Abs")); // Core.u
        al2.add(new TNativeFunction(187, nffFunction, 0, "Sin")); // Core.u
        al2.add(new TNativeFunction(188, nffFunction, 0, "Cos")); // Core.u
        al2.add(new TNativeFunction(189, nffFunction, 0, "Tan")); // Core.u
        al2.add(new TNativeFunction(190, nffFunction, 0, "Atan")); // Core.u
        al2.add(new TNativeFunction(191, nffFunction, 0, "Exp")); // Core.u
        al2.add(new TNativeFunction(192, nffFunction, 0, "Loge")); // Core.u
        al2.add(new TNativeFunction(193, nffFunction, 0, "Sqrt")); // Core.u
        al2.add(new TNativeFunction(194, nffFunction, 0, "Square")); // Core.u
        al2.add(new TNativeFunction(195, nffFunction, 0, "FRand")); // Core.u
        al2.add(new TNativeFunction(196, nffOperator, 22, ">>>")); // Core.u
        al2.add(new TNativeFunction(203, nffOperator, 26, "!=")); // Core.u
        al2.add(new TNativeFunction(210, nffOperator, 24, "~=")); // Core.u
        al2.add(new TNativeFunction(211, nffPreOperator, 0, "-")); // Core.u
        al2.add(new TNativeFunction(212, nffOperator, 16, "*")); // Core.u
        al2.add(new TNativeFunction(213, nffOperator, 16, "*")); // Core.u
        al2.add(new TNativeFunction(214, nffOperator, 16, "/")); // Core.u
        al2.add(new TNativeFunction(215, nffOperator, 20, "+")); // Core.u
        al2.add(new TNativeFunction(216, nffOperator, 20, "-")); // Core.u
        al2.add(new TNativeFunction(217, nffOperator, 24, "==")); // Core.u
        al2.add(new TNativeFunction(218, nffOperator, 26, "!=")); // Core.u
        al2.add(new TNativeFunction(219, nffOperator, 16, "Dot")); // Core.u
        al2.add(new TNativeFunction(220, nffOperator, 16, "Cross")); // Core.u
        al2.add(new TNativeFunction(221, nffOperator, 34, "*=")); // Core.u
        al2.add(new TNativeFunction(222, nffOperator, 34, "/=")); // Core.u
        al2.add(new TNativeFunction(223, nffOperator, 34, "+=")); // Core.u
        al2.add(new TNativeFunction(224, nffOperator, 34, "-=")); // Core.u
        al2.add(new TNativeFunction(225, nffFunction, 0, "VSize")); // Core.u
        al2.add(new TNativeFunction(226, nffFunction, 0, "Normal")); // Core.u
        al2.add(new TNativeFunction(227, nffFunction, 0, "Invert")); // Core.u
        al2.add(new TNativeFunction(229, nffFunction, 0, "GetAxes")); // Core.u
        al2.add(new TNativeFunction(230, nffFunction, 0, "GetUnAxes")); // Core.u
        al2.add(new TNativeFunction(231, nffFunction, 0, "Log")); // Core.u
        al2.add(new TNativeFunction(232, nffFunction, 0, "Warn")); // Core.u
        al2.add(new TNativeFunction(233, nffFunction, 0, "Error")); // Engine.u
        al2.add(new TNativeFunction(234, nffFunction, 0, "Right")); // Core.u
        al2.add(new TNativeFunction(235, nffFunction, 0, "Caps")); // Core.u
        al2.add(new TNativeFunction(236, nffFunction, 0, "Chr")); // Core.u
        al2.add(new TNativeFunction(237, nffFunction, 0, "Asc")); // Core.u
        al2.add(new TNativeFunction(242, nffOperator, 24, "==")); // Core.u
        al2.add(new TNativeFunction(243, nffOperator, 26, "!=")); // Core.u
        al2.add(new TNativeFunction(244, nffFunction, 0, "FMin")); // Core.u
        al2.add(new TNativeFunction(245, nffFunction, 0, "FMax")); // Core.u
        al2.add(new TNativeFunction(246, nffFunction, 0, "FClamp")); // Core.u
        al2.add(new TNativeFunction(247, nffFunction, 0, "Lerp")); // Core.u
        al2.add(new TNativeFunction(248, nffFunction, 0, "Smerp")); // Core.u
        al2.add(new TNativeFunction(249, nffFunction, 0, "Min")); // Core.u
        al2.add(new TNativeFunction(250, nffFunction, 0, "Max")); // Core.u
        al2.add(new TNativeFunction(251, nffFunction, 0, "Clamp")); // Core.u
        al2.add(new TNativeFunction(252, nffFunction, 0, "VRand")); // Core.u
        al2.add(new TNativeFunction(254, nffOperator, 24, "==")); // Core.u
        al2.add(new TNativeFunction(255, nffOperator, 26, "!=")); // Core.u
        al2.add(new TNativeFunction(256, nffFunction, 0, "Sleep")); // Engine.u
        al2.add(new TNativeFunction(258, nffFunction, 0, "ClassIsChildOf")); // Core.u
        al2.add(new TNativeFunction(259, nffFunction, 0, "PlayAnim")); // Engine.u
        al2.add(new TNativeFunction(260, nffFunction, 0, "LoopAnim")); // Engine.u
        al2.add(new TNativeFunction(261, nffFunction, 0, "FinishAnim")); // Engine.u
        al2.add(new TNativeFunction(262, nffFunction, 0, "SetCollision")); // Engine.u
        al2.add(new TNativeFunction(263, nffFunction, 0, "HasAnim")); // Engine.u
        al2.add(new TNativeFunction(264, nffFunction, 0, "PlaySound")); // Engine.u
        al2.add(new TNativeFunction(266, nffFunction, 0, "Move")); // Engine.u
        al2.add(new TNativeFunction(267, nffFunction, 0, "SetLocation")); // Engine.u
        al2.add(new TNativeFunction(272, nffFunction, 0, "SetOwner")); // Engine.u
        al2.add(new TNativeFunction(275, nffOperator, 22, "<<")); // Core.u
        al2.add(new TNativeFunction(276, nffOperator, 22, ">>")); // Core.u
        al2.add(new TNativeFunction(277, nffFunction, 0, "Trace")); // Engine.u
        al2.add(new TNativeFunction(278, nffFunction, 0, "Spawn")); // Engine.u
        al2.add(new TNativeFunction(279, nffFunction, 0, "Destroy")); // Engine.u
        al2.add(new TNativeFunction(280, nffFunction, 0, "SetTimer")); // Engine.u
        al2.add(new TNativeFunction(281, nffFunction, 0, "IsInState")); // Core.u
        al2.add(new TNativeFunction(282, nffFunction, 0, "IsAnimating")); // Engine.u
        al2.add(new TNativeFunction(283, nffFunction, 0, "SetCollisionSize")); // Engine.u
        al2.add(new TNativeFunction(284, nffFunction, 0, "GetStateName")); // Core.u
        al2.add(new TNativeFunction(287, nffOperator, 16, "*")); // Core.u
        al2.add(new TNativeFunction(288, nffOperator, 16, "*")); // Core.u
        al2.add(new TNativeFunction(289, nffOperator, 16, "/")); // Core.u
        al2.add(new TNativeFunction(290, nffOperator, 34, "*=")); // Core.u
        al2.add(new TNativeFunction(291, nffOperator, 34, "/=")); // Core.u
        al2.add(new TNativeFunction(293, nffFunction, 0, "GetAnimGroup")); // Engine.u
        al2.add(new TNativeFunction(294, nffFunction, 0, "TweenAnim")); // Engine.u
        al2.add(new TNativeFunction(296, nffOperator, 16, "*")); // Core.u
        al2.add(new TNativeFunction(297, nffOperator, 34, "*=")); // Core.u
        al2.add(new TNativeFunction(298, nffFunction, 0, "SetBase")); // Engine.u
        al2.add(new TNativeFunction(299, nffFunction, 0, "SetRotation")); // Engine.u
        al2.add(new TNativeFunction(300, nffFunction, 0, "MirrorVectorByNormal")); // Core.u
        al2.add(new TNativeFunction(301, nffFunction, 0, "FinishInterpolation")); // Engine.u
        al2.add(new TNativeFunction(303, nffFunction, 0, "IsA")); // Core.u
        al2.add(new TNativeFunction(304, nffFunction, 0, "AllActors")); // Engine.u
        al2.add(new TNativeFunction(305, nffFunction, 0, "ChildActors")); // Engine.u
        al2.add(new TNativeFunction(306, nffFunction, 0, "BasedActors")); // Engine.u
        al2.add(new TNativeFunction(307, nffFunction, 0, "TouchingActors")); // Engine.u
        al2.add(new TNativeFunction(308, nffFunction, 0, "ZoneActors")); // Engine.u
        al2.add(new TNativeFunction(309, nffFunction, 0, "TraceActors")); // Engine.u
        al2.add(new TNativeFunction(310, nffFunction, 0, "RadiusActors")); // Engine.u
        al2.add(new TNativeFunction(311, nffFunction, 0, "VisibleActors")); // Engine.u
        al2.add(new TNativeFunction(312, nffFunction, 0, "VisibleCollidingActors")); // Engine.u
        al2.add(new TNativeFunction(314, nffFunction, 0, "Warp")); // Engine.u
        al2.add(new TNativeFunction(315, nffFunction, 0, "UnWarp")); // Engine.u
        al2.add(new TNativeFunction(316, nffOperator, 20, "+")); // Core.u
        al2.add(new TNativeFunction(317, nffOperator, 20, "-")); // Core.u
        al2.add(new TNativeFunction(318, nffOperator, 34, "+=")); // Core.u
        al2.add(new TNativeFunction(319, nffOperator, 34, "-=")); // Core.u
        al2.add(new TNativeFunction(320, nffFunction, 0, "RotRand")); // Core.u
        al2.add(new TNativeFunction(464, nffFunction, 0, "StrLen")); // Engine.u
        al2.add(new TNativeFunction(465, nffFunction, 0, "DrawText")); // Engine.u
        al2.add(new TNativeFunction(466, nffFunction, 0, "DrawTile")); // Engine.u
        al2.add(new TNativeFunction(467, nffFunction, 0, "DrawActor")); // Engine.u
        al2.add(new TNativeFunction(468, nffFunction, 0, "DrawTileClipped")); // Engine.u
        al2.add(new TNativeFunction(469, nffFunction, 0, "DrawTextClipped")); // Engine.u
        al2.add(new TNativeFunction(470, nffFunction, 0, "TextSize")); // Engine.u
        al2.add(new TNativeFunction(471, nffFunction, 0, "DrawClippedActor")); // Engine.u
        al2.add(new TNativeFunction(472, nffFunction, 0, "DrawText")); // Engine.u
        al2.add(new TNativeFunction(473, nffFunction, 0, "DrawTile")); // Engine.u
        al2.add(new TNativeFunction(474, nffFunction, 0, "DrawColoredText")); // Engine.u
        al2.add(new TNativeFunction(475, nffFunction, 0, "ReplaceTexture")); // Engine.u
        al2.add(new TNativeFunction(476, nffFunction, 0, "TextSize")); // Engine.u
        al2.add(new TNativeFunction(480, nffFunction, 0, "DrawPortal")); // Engine.u
        al2.add(new TNativeFunction(500, nffFunction, 0, "MoveTo")); // Engine.u
        al2.add(new TNativeFunction(502, nffFunction, 0, "MoveToward")); // Engine.u
        al2.add(new TNativeFunction(504, nffFunction, 0, "StrafeTo")); // Engine.u
        al2.add(new TNativeFunction(506, nffFunction, 0, "StrafeFacing")); // Engine.u
        al2.add(new TNativeFunction(508, nffFunction, 0, "TurnTo")); // Engine.u
        al2.add(new TNativeFunction(510, nffFunction, 0, "TurnToward")); // Engine.u
        al2.add(new TNativeFunction(512, nffFunction, 0, "MakeNoise")); // Engine.u
        al2.add(new TNativeFunction(514, nffFunction, 0, "LineOfSightTo")); // Engine.u
        al2.add(new TNativeFunction(517, nffFunction, 0, "FindPathToward")); // Engine.u
        al2.add(new TNativeFunction(518, nffFunction, 0, "FindPathTo")); // Engine.u
        al2.add(new TNativeFunction(519, nffFunction, 0, "describeSpec")); // Engine.u
        al2.add(new TNativeFunction(520, nffFunction, 0, "actorReachable")); // Engine.u
        al2.add(new TNativeFunction(521, nffFunction, 0, "pointReachable")); // Engine.u
        al2.add(new TNativeFunction(522, nffFunction, 0, "ClearPaths")); // Engine.u
        al2.add(new TNativeFunction(523, nffFunction, 0, "EAdjustJump")); // Engine.u
        al2.add(new TNativeFunction(524, nffFunction, 0, "FindStairRotation")); // Engine.u
        al2.add(new TNativeFunction(525, nffFunction, 0, "FindRandomDest")); // Engine.u
        al2.add(new TNativeFunction(526, nffFunction, 0, "PickWallAdjust")); // Engine.u
        al2.add(new TNativeFunction(527, nffFunction, 0, "WaitForLanding")); // Engine.u
        al2.add(new TNativeFunction(529, nffFunction, 0, "AddPawn")); // Engine.u
        al2.add(new TNativeFunction(530, nffFunction, 0, "RemovePawn")); // Engine.u
        al2.add(new TNativeFunction(531, nffFunction, 0, "PickTarget")); // Engine.u
        al2.add(new TNativeFunction(532, nffFunction, 0, "PlayerCanSeeMe")); // Engine.u
        al2.add(new TNativeFunction(533, nffFunction, 0, "CanSee")); // Engine.u
        al2.add(new TNativeFunction(534, nffFunction, 0, "PickAnyTarget")); // Engine.u
        al2.add(new TNativeFunction(536, nffFunction, 0, "SaveConfig")); // Core.u
        al2.add(new TNativeFunction(539, nffFunction, 0, "GetMapName")); // Engine.u
        al2.add(new TNativeFunction(540, nffFunction, 0, "FindBestInventoryPath")); // Engine.u
        al2.add(new TNativeFunction(544, nffFunction, 0, "ResetKeyboard")); // Engine.u
        al2.add(new TNativeFunction(545, nffFunction, 0, "GetNextSkin")); // Engine.u
        al2.add(new TNativeFunction(546, nffFunction, 0, "UpdateURL")); // Engine.u
        al2.add(new TNativeFunction(547, nffFunction, 0, "GetURLMap")); // Engine.u
        al2.add(new TNativeFunction(548, nffFunction, 0, "FastTrace")); // Engine.u
        al2.add(new TNativeFunction(549, nffOperator, 20, "-")); // Engine.u
        al2.add(new TNativeFunction(550, nffOperator, 16, "*")); // Engine.u
        al2.add(new TNativeFunction(551, nffOperator, 20, "+")); // Engine.u
        al2.add(new TNativeFunction(552, nffOperator, 16, "*")); // Engine.u
        al2.add(new TNativeFunction(1033, nffFunction, 0, "RandRange")); // Core.u
        al2.add(new TNativeFunction(3969, nffFunction, 0, "MoveSmooth")); // Engine.u
        al2.add(new TNativeFunction(3970, nffFunction, 0, "SetPhysics")); // Engine.u
        al2.add(new TNativeFunction(3971, nffFunction, 0, "AutonomousPhysics")); // Engine.u

        return new TNativeFunctions(al2);
    }

    /*
    class test()
    {

    }*/

}
